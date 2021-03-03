from itertools import combinations

for j in range(10) :
    n=j+1

    items = []
    for i in range(n) :
        items.append(i+1)

    # print(items)
    total = 0

    for i in range(n) :
        r = i + 1
        comb = list(combinations(items, r))
        total = total + len(comb)
        # print(r, ' : ', len(comb), comb)

    print("Total : ", total)