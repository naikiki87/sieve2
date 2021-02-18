cnt = 15

while True :
    res = 0
    for i in range(cnt) :
        for j in range(cnt) :
            for k in range(cnt) :
                for m in range(cnt) :
                    res = res + i*j*k*m
    
    print(res)