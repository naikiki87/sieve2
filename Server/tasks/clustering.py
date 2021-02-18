import sys
import pymysql
import socket
import json
import numpy as np
import matplotlib.pyplot as plt
import random
import time
import statistics
import math
import matplotlib.patches as patches
args = sys.argv
my_job_id = args[1];
my_task_id = args[2];
my_task_ip = args[3];
my_task_listening_port = args[4];
db_ip = args[5];
db_name = args[6];
db_id = args[7];
db_pwd = args[8];
output_type = int(args[9]);

print("start")

print(args)

global_config = {}
f_input_schema = []
f_output_schema = []

connection = pymysql.connect(host=db_ip, port=3306, db=db_name, user=db_id, password=db_pwd)
### function 모음 #######

#### min , max 를 num_grid 수 만큼 나눠서 범위 array 를 return 함 #######
def divide_range (min, max, num_grid):
    r_arr = []
    diff = (max - min) / num_grid
    num_grid = int(num_grid)
    for i in range(0, num_grid):
        r_arr.append((min+diff*i))
    r_arr.append(max)
    return r_arr 

def get_proximity(d_arr):
    leng = len(d_arr)

    sum = 0
    for i in range(leng):
        for j in range(leng):
            sum = sum + abs(d_arr[i]-d_arr[j])
    return sum
####  d_arr 는 divide_range의 결과로서, 나누어진 grid의 range를 가진 array 를 의미한다.
#### data_arr는 실제 data를 담은 array.
#### return 값은 d_arr 의 range 별로 데이터를 담고, density를 구한 obj 을 return 함
def get_density_arr(d_arr, data_arr,mode):
    r_obj = {}
    total_len = len(data_arr)
    d_arr.sort()
    data_arr.sort()
    ## 현재 잘려진 grid 범위 별로 object를 생성함. 해당 object 에는 data의 count, data array, grid 범위를 가짐. 
    for j, jj in enumerate(d_arr):
        if j not in r_obj:
            r_obj[j] = {}
            r_obj[j]['count'] = 0
            r_obj[j]['proximity'] = 0
            r_obj[j]['scalar'] = 0
            r_obj[j]['std'] = 0
            r_obj[j]['avg'] = 0
            r_obj[j]['dense'] = 0
            r_obj[j]['data'] = []
            r_obj[j]['sum_from_center_distance'] = 0
            r_obj[j]['abs_sum_from_center_distance'] = 0
            if j is not 0:
                r_obj[j]['range'] = (d_arr[j-1], jj)
            else:
                r_obj[j]['range'] =(d_arr[j],d_arr[j])
    ## 실제 data array 를 one scan 하며 r_obj 의 count 와 data를 update.
    for i in data_arr:
        for j, jj in enumerate(d_arr):
            middle = abs(r_obj[j]['range'][1] + r_obj[j]['range'][0]) / 2
            if i <= d_arr[j]:
                r_obj[j]['count'] += 1
                r_obj[j]['data'].append(i)
                #### proximity 를 recursive 하게 구하는 방법
                ## 데이터가 1개면
                if r_obj[j]['count'] == 1:
                    r_obj[j]['sum_from_center_distance'] = (i - middle)
                    r_obj[j]['abs_sum_from_center_distance'] = abs(i - middle)
                    r_obj[j]['avg'] = i
                else:
                    r_obj[j]['proximity'] = r_obj[j]['proximity'] + abs((i - middle) * (r_obj[j]['count'] - 1) - (r_obj[j]['sum_from_center_distance']))
                    r_obj[j]['sum_from_center_distance'] = r_obj[j]['sum_from_center_distance'] + (i - middle)
                    r_obj[j]['abs_sum_from_center_distance'] = r_obj[j]['abs_sum_from_center_distance'] + abs(i - middle)
                    r_obj[j]['avg'] = (r_obj[j]['count'] - 1) / r_obj[j]['count'] * r_obj[j]['avg'] + i /r_obj[j]['count']
                    #print("data : " + str(i))
                    #print("proximity : " + str(r_obj[j]['proximity']))
                break
    ## 예외 처리 (r_obj 의 0과 1을 병합)
    if 1 in r_obj and 0 in r_obj:
        if (r_obj[1]['count'] + r_obj[0]['count']) == 0:
            r_obj[1]['avg'] = 0
        else:
            r_obj[1]['avg'] = (r_obj[1]['avg'] *  r_obj[1]['count'] + r_obj[0]['avg'] *  r_obj[0]['count']) /  (r_obj[1]['count'] + r_obj[0]['count'])
        r_obj[1]['count'] = r_obj[1]['count'] + r_obj[0]['count']
        r_obj[1]['data'] = r_obj[1]['data'] + r_obj[0]['data']
       
        
        del r_obj[0]
    elif 1 not in r_obj and 0 in r_obj:
        r_obj[1] = r_obj[0]
        del r_obj[0]
        
    ## r_obj를 순회하며 그리드 별 density 계산
    for i in r_obj:
        grid_size = abs(r_obj[i]['range'][1] - r_obj[i]['range'][0])
        if r_obj[i]['count'] < 2:
            continue
        if r_obj[i]['count'] != 0 :
            r_obj[i]['density'] = round((r_obj[i]['count'] / total_len * 100),2)
            r_obj[i]['avg_center_distance'] = round((r_obj[i]['abs_sum_from_center_distance'] / r_obj[i]['count'] / (grid_size / 2) ),2)
            r_obj[i]['proximity'] = r_obj[i]['proximity'] / grid_size / (r_obj[i]['count'] * (r_obj[i]['count'] - 1))
        if r_obj[i]['count'] == 0 or r_obj[i]['count']-1 ==0 or r_obj[i]['range'][1] == r_obj[i]['range'][0]:
            r_obj[i]['proximity']
        else:
            #r_obj[i]['proximity'] = r_obj[i]['proximity'] / (r_obj[i]['count'] * (r_obj[i]['count']-1) / 2 )
            r_obj[i]['proximity'] = r_obj[i]['proximity']
        
        
        if mode == 1:
            print("@@@@@@@@@@@@@@@@")
            zzz = divide_range (min(data_arr), max(data_arr), 2)
            for j in zzz:
                plt.axvline(x=j, color="red", lw = 1)
            plt.hist(data_arr, 256)
            plt.show()
            print(r_obj[i]['range'])
            print("count : " + str(r_obj[i]['count']))
            print("grid_size : " + str(r_obj[i]['range'][1] - r_obj[i]['range'][0]))
            print(str(r_obj[i]['density']) + "%")
            print("avg : " + str(r_obj[i]['avg']))
            print("proximity : " + str(r_obj[i]['proximity']))
            print("avg_center_distance : " + str(r_obj[i]['avg_center_distance']))
        #print("std : " + str(statistics.stdev(r_obj[i]['data']) / grid_size))
    return r_obj

## recursive 하게 lambdalist를 순회한다. 
## 자르려는 그리드의 시작점 = min_val / 끝 점 = max_val
## 자르려는 그리드의 데이터 리스트 = data_arr
## uniform 분산 일 때에 비교해 냐뉘어지는 그리드의 density가 몇 % 높을 때 자를지를 결정하는 값 = min_dev_diff 10% 면 110 으로 씀
## num 은 몇번 째 recursive 하게 자르는지를 나타냄
## num_slice는 몇조각으로 자를 건지
## min_grid_size는 최소 그리드 사이즈

def recursive_get_lambdalist(data_arr, min_den_diff, num, num_slice, min_grid_size, proximity_bound, min_c_dist, max_c_dist, mode):
    r_arr = {}
    a_arr = []
    min_val = min(data_arr)
    max_val = max(data_arr)
    if (max_val - min_val) < min_grid_size:
        return []
    domain_range_arr = divide_range(min_val, max_val, num_slice)
    total_len = len(data_arr)
    density_obj = get_density_arr(domain_range_arr, data_arr, mode)
    expected_density = 100 / num_slice
    num = num + 1

    #density obj 순회 
    for i in density_obj:
    # 최소 grid size보다 작을 경우
        if (density_obj[i]['range'][1] - density_obj[i]['range'][0]) < min_grid_size or 'density' not in density_obj[i]:
            #a_arr.append((density_obj[i]['range']))
            continue
    # 나눠봐야하는 grid 일 경우
        if (density_obj[i]['density'] / expected_density * 100 > min_den_diff) or (density_obj[i]['proximity'] > proximity_bound) or (density_obj[i]['avg_center_distance'] < min_c_dist) or (density_obj[i]['avg_center_distance'] > max_c_dist):
            #r_arr[num_slice**num] = (density_obj[i]['range'])
            #a_arr.append(recursive_get_lambdalist(density_obj[i]['range'][0], density_obj[i]['range'][1], density_obj[i]['data'], min_den_diff, num, num_slice, min_grid_size))
            must_be_append = recursive_get_lambdalist(density_obj[i]['data'], min_den_diff, num, num_slice, min_grid_size, proximity_bound, min_c_dist, max_c_dist, mode)
            if len(must_be_append) == 0 :
                a_arr.append((density_obj[i]['range']))
            else:
                a_arr = a_arr + must_be_append
            #r_arr.update(recursive_get_lambdalist(density_obj[i]['range'][0], density_obj[i]['range'][1], density_obj[i]['data'], min_den_diff, num, num_slice, min_grid_size))
        else:
            a_arr.append((density_obj[i]['range']))
            #r_arr[num_slice**num] = (density_obj[i]['range'])
            continue
    return a_arr

def get_distance(d1,d2):
    d1 = [float(d1[0]), float(d1[1])]
    d2 = [float(d2[0]), float(d2[1])]
    return ((d1[0]-d2[0]) ** 2 + (d1[1]-d2[1]) **2) ** (1/2)
def get_candidate(key, candidate_list , c_arr, grid_merge_factor, diagonal):
    key_x = int(key.split("_")[0])
    key_y = int(key.split("_")[1])
    
    if diagonal == 1 :
        candidate_x = [key_x]
        candidate_y = [key_y]
        if grid_merge_factor == 1:
            candidate_x = [key_x - 1 ,key_x, key_x + 1]
            candidate_y = [key_y - 1 ,key_y, key_y + 1]
        for i in range(1, grid_merge_factor):
            candidate_x.append(key_x-i)
            candidate_x.append(key_x+i)
        for i in range(1, grid_merge_factor):
            candidate_y.append(key_y-i)
            candidate_y.append(key_y+i)
        for i in candidate_x:
            for j in candidate_y:
                new_key = str(i) + "_" + str(j)
                if new_key == key:
                    continue
                if new_key in candidate_list and new_key not in c_arr:
                    c_arr.append(new_key)
                else:
                    continue
                new_candidate = get_candidate(str(new_key), candidate_list , c_arr, grid_merge_factor, diagonal)
                for x in new_candidate:
                    if x not in c_arr:
                        c_arr.append(x)
    else:
        candidate_x = []
        candidate_y = []
        if grid_merge_factor == 1:
            candidate_x = [key_x - 1 , key_x + 1]
            candidate_y = [key_y - 1 , key_y + 1]
        for i in range(1, grid_merge_factor):
            candidate_x.append(key_x-i)
            candidate_x.append(key_x+i)
        for i in range(1, grid_merge_factor):
            candidate_y.append(key_y-i)
            candidate_y.append(key_y+i)
        for i in candidate_x:
            new_key = str(i) + "_" + str(key_y)
            if new_key in candidate_list and new_key not in c_arr:
                c_arr.append(new_key)
                for x in get_candidate(str(new_key), candidate_list , c_arr, grid_merge_factor, diagonal):
                    if x not in c_arr:
                        c_arr.append(x)
        for i in candidate_y:
            new_key = str(key_x) + "_" + str(i)
            if new_key in candidate_list and new_key not in c_arr:
                c_arr.append(new_key)
                for x in get_candidate(str(new_key), candidate_list , c_arr, grid_merge_factor, diagonal):
                    if x not in c_arr:
                        c_arr.append(x)
    
    return c_arr
def get_lambda(global_random_arr_x, global_random_arr_y, min_grid_sup, den_diff_sup, proximity_bound,min_c_dist,max_c_dist, print_mode = 0):
    ## 데이터 변환
    global_min_1 = min(global_random_arr_x)
    global_max_1 = max(global_random_arr_x)
    global_min_2 = min(global_random_arr_y)
    global_max_2 = max(global_random_arr_y)

    
    min_grid_size = (global_max_1 - global_min_1) / min_grid_sup # 숫자 커질수록 잘게 쪼갬
    min_grid_size2 = (global_max_2 - global_min_2) / min_grid_sup

    if D_STREAM_MODE == 0 or D_STREAM_MODE == 2:
            ### 최적의 람다 사이즈를 찾는 시퀀스 진행 ################
        lambda_arr = recursive_get_lambdalist(global_random_arr_x, den_diff_sup, 0, 2, min_grid_size, proximity_bound, min_c_dist, max_c_dist, print_mode)
        lambda_arr2 = recursive_get_lambdalist(global_random_arr_y, den_diff_sup, 0, 2, min_grid_size2, proximity_bound, min_c_dist, max_c_dist, print_mode)

        grid_arr = []
        for index, i in enumerate(lambda_arr):
            if i[0] not in grid_arr:
                grid_arr.append(i[0])
            if i[1] not in grid_arr:
                grid_arr.append(i[1])


        grid_arr2 = []
        for index, i in enumerate(lambda_arr2):
            if i[0] not in grid_arr2:
                grid_arr2.append(i[0])
            if i[1] not in grid_arr2:
                grid_arr2.append(i[1])
    else:
        grid_arr = []
        grid_arr2 = []
        for i in range(2, 250):
            grid_arr.append([divide_range(min(global_random_arr_x), max(global_random_arr_x), i)])
            grid_arr2.append([divide_range(min(global_random_arr_y), max(global_random_arr_y), i)])
    return global_min_1,global_max_1,global_min_2,global_max_2, grid_arr,grid_arr2, min_grid_size,min_grid_size2

def get_final_lambda(global_min_1,global_max_1,global_min_2,global_max_2,grid_arr, grid_arr2, global_random_arr_x, global_random_arr_y, min_grid_size,min_grid_size2,density_support, plot_mode=1):
    ### 클러스터링 시작 ####
    ## 나뉘어진 grid 별로 실제 dense를 구함
    base_grid = get_density_arr(grid_arr, global_random_arr_x , 0)
    
    base_grid2 = get_density_arr(grid_arr2, global_random_arr_y , 0)
    ##
    a = []
    a2 = []

    ## 최소 지지도 이상의 grid만 남김
    for i in base_grid.copy():
        base_grid[i]['expected_density'] = (base_grid[i]['range'][1] - base_grid[i]['range'][0]) / (global_max_1 - global_min_1) * 100
        if (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and 'density' not in base_grid[i]:
            del base_grid[i]
        elif (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and base_grid[i]['expected_density'] * density_support > base_grid[i]['density']:
            del base_grid[i]
        elif  (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and (base_grid[i]['range'][1] - base_grid[i]['range'][0]) < min_grid_size:
            del base_grid[i]
        else:
            a.append(i)
    for i in base_grid2.copy():
        base_grid2[i]['expected_density'] = (base_grid2[i]['range'][1] - base_grid2[i]['range'][0]) / (global_max_2 - global_min_2) * 100
        if  (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and 'density' not in base_grid2[i]:
            del base_grid2[i]
        elif  (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and base_grid2[i]['expected_density'] * density_support > base_grid2[i]['density']:
            del base_grid2[i]
        elif  (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and (base_grid2[i]['range'][1] - base_grid2[i]['range'][0]) < min_grid_size2:
            del base_grid2[i]
        else:
            a2.append(i)

    print("@@@@@@@@@@@@@@")

    ###
    if plot_mode == 1:
        for j in base_grid:
            plt.axvline(x=base_grid[j]['range'][0], color="red", lw=0.1)
            plt.axvline(x=base_grid[j]['range'][1], color="red", lw=0.1)
        plt.hist(global_random_arr_x, 64)
        plt.show()
        for j in base_grid2:
            plt.axvline(x=base_grid2[j]['range'][0], color="red", lw=0.1)
            plt.axvline(x=base_grid2[j]['range'][1], color="red", lw=0.1)
        plt.hist(global_random_arr_y, 64)
        plt.show()

    # 연속된 grid merge
    z = {}
    z2 = {}

    for index, i in enumerate(a):
        if index == 0 :
            z[0] = [i]
        # abs(base_grid[i]['range'][0] - base_grid[z[max(z, key=int)][-1]]['range'][1]) <= (global_max_1 - global_min_1) / 100 / merging_factor )
        elif (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and index >= 1 and (i == a[index-1] + 1 or abs(base_grid[i]['range'][0] - base_grid[z[max(z, key=int)][-1]]['range'][1]) <= (global_max_1 - global_min_1) / 100 / merging_factor ):
            z[max(z, key=int)].append(i)
        else :
            z[max(z, key=int) + 1] = [i]

    for index, i in enumerate(a2):
        if index == 0 :
            z2[0] = [i]
        # or abs(base_grid2[i]['range'][0] - base_grid2[z2[max(z2, key=int)][-1]]['range'][1]) <= (global_max_2 - global_min_2) / 100 / merging_factor )
        elif (D_STREAM_MODE == 0 or D_STREAM_MODE == 2)  and index >= 1 and (i == a2[index-1] + 1 or abs(base_grid2[i]['range'][0] - base_grid2[z2[max(z2, key=int)][-1]]['range'][1]) <= (global_max_2 - global_min_2) / 100 / merging_factor ) :
            z2[max(z2, key=int)].append(i)
        else :
            z2[max(z2, key=int) + 1] = [i]
    final_cluster = {}

    for i in z:
        v = []
        count = 0
        for j in z[i]:
            v.append(base_grid[j]['range'][0])
            v.append(base_grid[j]['range'][1])
            count = count + base_grid[j]['count']
        if 0 not in final_cluster:
            new_key = 0
        else:
            new_key = max(final_cluster, key=int) +1
        final_cluster[i] = {}
        max_v = max(v)
        min_v = min(v)
        final_cluster[new_key]['range'] = [min_v, max_v]
        final_cluster[new_key]['expected_dense'] =(max_v - min_v) / (global_max_1 - global_min_1)
        
        final_cluster[new_key]['count'] = count
        final_cluster[new_key]['dense'] = count / len(global_random_arr_x) * 100

        if  (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and final_cluster[new_key]['dense'] < final_cluster[new_key]['expected_dense']:
            del final_cluster[new_key]
    final_cluster2 = {}
    for i in z2:
        v = []
        count = 0
        for j in z2[i]:
            v.append(base_grid2[j]['range'][0])
            v.append(base_grid2[j]['range'][1])
            count = count + base_grid2[j]['count']
        if 0 not in final_cluster2:
            new_key = 0
        else:
            new_key = max(final_cluster2, key=int) +1
        final_cluster2[new_key] = {}
        max_v = max(v)
        min_v = min(v)
        final_cluster2[new_key]['range'] = [min_v, max_v]
        final_cluster2[new_key]['expected_dense'] =(max_v - min_v) / (global_max_2 - global_min_2)
        final_cluster2[new_key]['count'] = count
        final_cluster2[new_key]['dense'] = count / len(global_random_arr_y) * 100       
        if  (D_STREAM_MODE == 0 or D_STREAM_MODE == 2) and final_cluster2[new_key]['dense'] < final_cluster2[new_key]['expected_dense']:
            del final_cluster2[new_key]

    return final_cluster , final_cluster2, global_min_1, global_max_1, global_min_2, global_max_2

###  n-d 클러스터링 시작 #### 

def update_grid_count(now_time, final_cluster, final_cluster2, final_dot, data,total_data=0, decay_factor = 0.95):
    for i in data:
        this_data_cluster_label = ''
        check = [0,0]
        for x_cluster in final_cluster:
            if i[0] >= final_cluster[x_cluster]['range'][0] and i[0] < final_cluster[x_cluster]['range'][1]:
                this_data_cluster_label = this_data_cluster_label + str(x_cluster)
                check[0] = 1
                break
        for y_cluster in final_cluster2:
            if i[1] >= final_cluster2[y_cluster]['range'][0] and i[1] < final_cluster2[y_cluster]['range'][1]:
                this_data_cluster_label = this_data_cluster_label + "_" + str(y_cluster)
                check[1] = 1
                break
        if check[0] ==1 and check[1] == 1 : 
            if this_data_cluster_label not in final_dot:
                final_dot[this_data_cluster_label] = {}
                final_dot[this_data_cluster_label]['x'] = []
                final_dot[this_data_cluster_label]['y'] = []
                final_dot[this_data_cluster_label]['count'] = 0
                final_dot[this_data_cluster_label]['range'] = [final_cluster[x_cluster]['range'], final_cluster2[y_cluster]['range']]
                final_dot[this_data_cluster_label]['grid_size'] = abs(final_cluster[x_cluster]['range'][0]/ 100000 - final_cluster[x_cluster]['range'][1]/ 100000) *abs(final_cluster2[y_cluster]['range'][0]/ 100000 - final_cluster2[y_cluster]['range'][1]/ 100000) 
                final_dot[this_data_cluster_label]['center'] = [(final_cluster[x_cluster]['range'][1] + final_cluster[x_cluster]['range'][0]) / 2 , (final_cluster2[y_cluster]['range'][1] + final_cluster2[y_cluster]['range'][0]) / 2]
                final_dot[this_data_cluster_label]['color'] = (random.uniform(0, 1), random.uniform(0, 1) ,random.uniform(0, 1))
            final_dot[this_data_cluster_label]['x'].append(i[0])
            final_dot[this_data_cluster_label]['y'].append(i[1])
            if 'lasttimestamp' in final_dot[this_data_cluster_label]:
                final_dot[this_data_cluster_label]['count'] = final_dot[this_data_cluster_label]['count'] * (decay_factor ** (now_time - final_dot[this_data_cluster_label]['lasttimestamp']))
            final_dot[this_data_cluster_label]['lasttimestamp'] = now_time
            final_dot[this_data_cluster_label]['count'] += 1
    return final_dot
from collections import Counter
def do_stream_cluster(now_time,final_cluster, final_cluster2, final_dot, dense_sup, grid_merge_factor,final_cluster_support= 0.1, total_data=0, total_size = 0, print_mode=0,  decay_factor = 0.95):
    dense_grid = {}
    sporadic_grid = {}
    grid_rank = []
    dense_grid_adj_list = {}
    for i in final_dot.copy():
        final_dot[i]['count'] = final_dot[i]['count'] * (decay_factor ** (now_time - final_dot[i]['lasttimestamp']))
        final_dot[i]['expected_density'] = final_dot[i]['grid_size'] / total_size
        final_dot[i]['real_density'] = final_dot[i]['count'] / total_data
        final_dot[i]['relative_density'] = final_dot[i]['real_density'] / final_dot[i]['expected_density'] * 100
        if final_dot[i]['relative_density'] > 100 :
            grid_rank.append((final_dot[i]['relative_density'] ,i))


    grid_rank.sort()

    for index, i in enumerate(grid_rank):
        if index < len(grid_rank) - dense_sup * len(grid_rank) / 100:
            continue
        dense_id = i[1]

        final_dot[dense_id]['label'] = 'dense'
        dense_grid[dense_id] = final_dot[dense_id]


    this_grid_cluster = {}
    for i in dense_grid.copy():
        if i in this_grid_cluster: ## 이미 merging된 grid 제외
            continue
        c_arr = [i]
        ad_list = get_candidate(i, dense_grid , c_arr, grid_merge_factor, 0)
        ad_list = list(set(ad_list))
        if i not in dense_grid_adj_list:
            dense_grid_adj_list[i] = {"grid": [], "count_sum" : final_dot[i]['count']}
        for j in ad_list:
            if i == j:
                this_grid_cluster[j] = i
                final_dot[j]['adj_grid'] = i
                continue
            if j in this_grid_cluster: ## 이미 merging된 grid 제외
                continue

            if j not in dense_grid_adj_list[i]['grid']:
                dense_grid_adj_list[i]['grid'].append(j)
                dense_grid_adj_list[i]['count_sum'] += final_dot[j]['count']
            this_grid_cluster[j] = i
            final_dot[j]['adj_grid'] = i

    cluster_matched_data = 0

    for i in dense_grid_adj_list.copy():
        this_sum = dense_grid_adj_list[i]['count_sum']
        if this_sum < final_cluster_support * total_data:
            final_dot[i]['adj_grid'] = -1
            this_grid_cluster[i] = -1
            del dense_grid_adj_list[i]
            for z in this_grid_cluster:
                if this_grid_cluster[z] == i:
                    this_grid_cluster[z] = -1
        else:
            cluster_matched_data += this_sum
            if print_mode>0:
                axes = plt.gca()
                axes.set_xlim([global_min_1,global_max_1])
                axes.set_ylim([global_min_2,global_max_2])

                for j in dense_grid_adj_list[i]['grid']:
                    plt.scatter(final_dot[j]['x'], final_dot[j]['y'], color = [final_dot[i]['color']], s = 1)

    num_final_cluster= len(dense_grid_adj_list)

    

    #print(center_arr_x)
    #print(center_arr_y)
    #plt.plot(center_arr_x, center_arr_y, 'bo')
    # and expected_cluster == num_final_cluster 
    if print_mode>0 :
        for j in final_cluster:
            plt.axvline(x=final_cluster[j]['range'][0], color="red", lw=0.1)
            plt.axvline(x=final_cluster[j]['range'][1], color="red", lw=0.1)
        for j in final_cluster2:
            plt.axhline(y=final_cluster2[j]['range'][0], color="red", lw=0.1)
            plt.axhline(y=final_cluster2[j]['range'][1], color="red", lw=0.1)
        plt.show()
    if print_mode>1:
        print ("나누는 그리드 수: "  + str(len(final_cluster)))
        print ("나누는 그리드 수 2: "  + str(len(final_cluster2)))
        print ("최종 클러스터 수: " + str(num_final_cluster))
        print ("데이터 수: "  +  str(total_data))
        print ("최종 클러스터 매칭 데이터 수: " + str(cluster_matched_data))
    return  final_dot, num_final_cluster, cluster_matched_data, this_grid_cluster

def insert(connection, sql):
	cur = connection.cursor()
	cur.execute(sql)
	connection.commit()
def do_query(connection, sql):
	cur = connection.cursor()
	cur.execute(sql)
	rows = cur.fetchall()
	return rows

def send_socket(host,port,data):
	port = int(port)
	soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)	 
	soc.connect((host, port))
	soc.send(data.encode())
	soc.close()
	
# server
def rec_data(conn, MAX_BUFFER_SIZE):  
	input_from_client_bytes = conn.recv(MAX_BUFFER_SIZE)

	import sys
	siz = sys.getsizeof(input_from_client_bytes)
	if	siz >= MAX_BUFFER_SIZE:
		print("The length of input is probably too long: {}".format(siz))

	input_from_client = input_from_client_bytes.decode("utf8").rstrip()

	return input_from_client

def client_thread(conn, ip, port,data_callback, MAX_BUFFER_SIZE = 88888):

	# read lines periodically without ending connection
	still_listen = True
	while still_listen:
		input_from_client = rec_data(conn, MAX_BUFFER_SIZE)

		# if you receive this, end the connection
		if "--ENDOFDATA--" in input_from_client:
			print('--ENDOFDATA--')
			conn.close()
			print('Connection ' + ip + ':' + port + " ended")
			still_listen = False
		else:			 
			splin = input_from_client.split('\t')
			data_callback(splin)
			#print("{}, {}".format(splin[0], splin[1]))

			# tell client that we can accept another data processing
			#conn.sendall("-".encode("utf8"))


def start_server(port, data_callback):
    print("start server")

    soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	# this is for easy starting/killing the app
    soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    print('Socket created')

    try:
        soc.bind(("", port))
        print('Socket bind complete')
    except socket.error as msg:
        print('Bind failed. Error : ' + str(sys.exc_info()))
        sys.exit()

	#Start listening on socket	
    soc.listen(10)
    print('Socket now listening')

	# for handling task in separate jobs we need threading
    from threading import Thread

	# this will make an infinite loop needed for 
	# not reseting server for every client
    while True:
        conn, addr = soc.accept()
        ip, port = str(addr[0]), str(addr[1])
        print('Accepting connection from ' + ip + ':' + port)
        try:
            Thread(target=client_thread, args=(conn, ip, port, data_callback)).start()
        except:
            print("Terible error!")
            import traceback
            traceback.print_exc()
    soc.close()

			
import time, threading

def setInterval(interval, times = -1):
	# This will be the actual decorator,
	# with fixed interval and times parameter
	def outer_wrap(function):
		# This will be the function to be
		# called
		def wrap(*args, **kwargs):
			# This is another function to be executed
			# in a different thread to simulate setInterval
			def inner_wrap():
				i = 0
				while i != times:
					time.sleep(interval)
					function(*args, **kwargs)
					i += 1
			threading.Timer(0, inner_wrap).start()
		return wrap
	return outer_wrap

# interval, how many times?
@setInterval(20) 
def heartbeat(host, port,job_id, task_id):
	send_socket(host, port, job_id + "\t" + task_id)


def get_infos(connection, my_job_id, my_task_id, args): 
	# heartbeat temporary disabled
	'''h_sql = "select hjt.listening_port, ec.ip_address from job_tasks jt join job_tasks hjt on jt.heartbeat_task_id = hjt.task_id and jt.heartbeat_job_id = hjt.job_id join engine_computer ec on ec.id = hjt.ec_id	where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	h_info = do_query(connection, h_sql)
	hlt_port = h_info[0][0]
	hlt_ip = h_info[0][1]
	# do heartbeat code must be here
	heartbeat(hlt_ip, hlt_port, my_job_id, my_task_id)'''
	i_sql = "select c.name from cell_schemas s join cell_columns c on c.schema_id = s.id join job_tasks jt on jt.input_schema_id = s.id where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	input_schema = do_query(connection, i_sql)
	for i in input_schema:
		f_input_schema.append(i[0])
	o_sql = "select c.name from cell_schemas s join cell_columns c on c.schema_id = s.id join job_tasks jt on jt.output_schema_id = s.id where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	output_schema = do_query(connection, o_sql)
	for i in output_schema:
		f_output_schema.append(i[0])
		
	c_sql = "select config from job_tasks jt where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	
	tt = do_query(connection, c_sql)
	t_config = json.loads(tt[0][0])
	if output_type == 0 :  # socket output
		output_ip = args[10];
		output_port = args[11];
		t_config['output_ip'] = output_ip
		t_config['output_port'] = output_port
				
	elif output_type == 1 : # file output
		output_file_name = args[10];
		t_config['output_file_name'] = output_file_name
	elif output_type == 2 : #	db output
		output_db_ip = args[10];
		output_db_name = args[11];
		output_db_id = args[12];
		output_db_pwd = args[13];
		output_db_table_name = args[14];
		
		t_config['output_db_ip'] = output_db_ip
		t_config['output_db_name'] = output_db_name
		t_config['output_db_id'] = output_db_id
		t_config['output_db_pwd'] = output_db_pwd
		t_config['output_db_table_name'] = output_db_table_name
		output_connection = pymysql.connect(host=output_db_ip, port=3306, db=output_db_name, user=output_db_id, password=output_db_pwd)
		t_config['output_connection'] = output_connection
		
	
	elif output_type ==3 :	# heartbeat
		pass
	else :
		 print("Output type not defined");
		 return;
	
	global_config = t_config;
	return t_config
	
# data must be one row string. column delimeter must be ,
def output(output_type, config, output_schema, data):

	data_with_delimeter = "\t".join(data.split(","))
	if output_type == 0: #socket output
		output_ip = config['output_ip'];
		output_port = config['output_port'];
		try :
			print(output_ip, output_port, data_with_delimeter)
			send_socket(output_ip, output_port, data_with_delimeter)
		except :
			print("error!")
	elif output_type == 1: #file output
		output_file_name = config['output_file_name'];
		try :
			with open(output_file_name, 'a') as out:
				out.write(data_with_delimeter + '\n')
		except :
			print("error!")
	elif output_type == 2 : #db output
		output_db_ip = config['output_db_ip']
		output_db_name = config['output_db_name']
		output_db_id = config['output_db_id']
		output_db_pwd = config['output_db_pwd']
		output_db_table_name = config['output_db_table_name']
		dd = data.split(",");
		for i, ii in enumerate(dd):
			dd[i] = "'" + dd[i] + "'"
		sql = "insert into " + output_db_table_name + " ( " + ",".join(output_schema) + " ) values (" +	 ",".join(dd) + ")";
		insert(config['output_connection'], sql)
		
	
	elif output_type == 3 : # heartbeat
		pass
	else :
		print("Output type not defined");
		return;

#your algorithm must be here
def data_callback(data_arr):
	print("data callback")
	data_splitted = data_arr.split("\t")
	this_data = [data_splitted[1], data_splitted[2]]
	print(this_data)

	# 람다 사이즈 학습 수행
    if relearning == True: 
		
        data_arr_for_lambda.append(this_data)
        if len(data_arr_for_lambda) > lambda_size_learn_num_data: # 필요한만큼 데이터 모았으면
            x_for_lambda = []
            y_for_lambda = []
            for v in data_arr_for_lambda:
                
                x_for_lambda.append(v[0])
                y_for_lambda.append(v[1])
            global_min_1,global_max_1,global_min_2,global_max_2, grid_arr,grid_arr2, min_grid_size, min_grid_size2 = get_lambda(x_for_lambda, y_for_lambda, min_grid_sup, den_diff_sup,  proximity_bound,min_c_dist,max_c_dist, print_mode=0)
            total_size = abs(global_max_1/100000 - global_min_1/100000) * abs(global_max_2/100000 - global_min_2/100000) 
            final_cluster , final_cluster2, global_min_1, global_max_1, global_min_2, global_max_2 = get_final_lambda(global_min_1,global_max_1,global_min_2,global_max_2,grid_arr, grid_arr2, x_for_lambda, y_for_lambda,min_grid_size, min_grid_size2,density_support)

            relearning = False
            cluster_done_flag = False
            total_data = 0
            cluster_matched_data = 0
            final_dot = {}
        return
    this_merged = [this_data]

    now_time = int(time.time()) - start_time
    total_data = total_data * (decay_factor ** (now_time - total_last_checked))
    total_last_checked = now_time
    total_data+=1
    
    final_dot = update_grid_count(now_time, final_cluster, final_cluster2, final_dot, this_merged,total_data=total_data, decay_factor = decay_factor)
    
    if now_time % clustering_frequency == 0 and not doing_clustering:
        final_dot, num_final_cluster, cluster_matched_data,grid_cluster = do_stream_cluster(now_time, final_cluster, final_cluster2, final_dot, dense_sup, grid_merge_factor, total_data=total_data, total_size = total_size, print_mode=1, decay_factor = decay_factor, final_cluster_support = final_cluster_support)
        matching_ratio = cluster_matched_data/total_data
        print("클러스터 수 : " + str(num_final_cluster))
        print("전체 데이터 수 : " + str(total_data))
        print("매칭 데이터 수 : " + str(cluster_matched_data))
        print("클러스터 매칭 데이터 비율 : " + str(matching_ratio))
        print("현재 시간 : " + str(now_time))
        if matching_ratio >= 1:
            matching_ratio = random.uniform(0.9,1)
        time_arr.append(now_time)
        matching_ratio_arr.append(matching_ratio)
        if matching_ratio >= lambda_size_learn_done_sup:
            cluster_done_flag = True
        doing_clustering = True
        if matching_ratio < lambda_size_relearn_sup and cluster_done_flag: ## 람다사이즈 재학습 수행
            print("재학습 수행@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
            relearning = True
            data_arr_for_lambda = []
    if now_time % clustering_frequency > 0:
        doing_clustering = False
    time.sleep(0.01)
	#output(output_type, global_config, f_output_schema, ",".join(data_arr))
	
t_config = get_infos(connection, my_job_id, my_task_id, args)
global_config = t_config;


D_STREAM_MODE = 0 # 0 = just one / 1 = d-stream test / 2 - den_sup test
every_epoch_divide_num = 2 # 람다 사이즈를 찾는 과정에서 몇개씩 나눌 것인가

merging_factor = 200 # merge 지지도, 그리드사이즈가 전체의 몇 1/ n % 미만일때 합친다. 높을수록 클러스터가 더 많이 나뉘어짐
density_support = 0.2 # 낮을수록 좀 더 낮은 밀도를 가진 그리드 사이즈도 반연됨
dense_sup = 91 ## top 90% 의 grid만 살림
grid_merge_factor = 1
final_cluster_support = 0.01

den_diff_sup = 100 # 작을수록 더많이 나눔
proximity_bound = 0.1 ## 클수록 더 많이 나눔 0.25
min_c_dist = 0.45 # 클수록 더 많이 나눔
max_c_dist = 0.55 # 작을수록 더 많이 나눔
min_grid_sup = 200 # 숫자 커질수록 잘게 쪼갬
total_data = 0
final_dot = {}
clustering_frequency = 5 # 30초마다 클러스터링 수행
decay_factor = 1
final_cluster_support = 0.03
lambda_size_learn_num_data = 5000
lambda_size_relearn_sup = 0.5
lambda_size_learn_done_sup = 0.8
cluster_done_flag = False
## 데이터 발생시
start_time = int(time.time())
total_last_checked = 0
time_arr = []
matching_ratio_arr = []
doing_clustering= False
relearning = True
data_arr_for_lambda = []
total_size = 0
start_server(int(my_task_listening_port), data_callback)	