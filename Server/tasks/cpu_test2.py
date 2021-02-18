import multiprocessing as mp

def work(job_list):
    return job_list + 1

p = mp.Pool(4)
p.map_async(work, job_list).get()