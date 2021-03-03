import pandas as pd

df = pd.DataFrame({'key': ['K0', 'K1', 'K2', 'K3', 'K4', 'K5'],
                   'A': ['A0', 'A1', 'A2', 'A3', 'A4', 'A5']})

other = pd.DataFrame({'key': ['K0', 'K1', 'K2'],
                      'B': ['B0', 'B1', 'B2']})

print(df)
print(other)

df_INNER_JOIN = pd.merge(df, other, left_on='key', right_on='key', how='inner')
print(df_INNER_JOIN)