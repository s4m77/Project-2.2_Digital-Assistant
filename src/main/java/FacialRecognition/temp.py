# ##73.64
# 73.64
# 73.64
# 73.64
# 73.64
# 72.84
# 71.67
# 71.26
# 70.36
# 68.42
# 68.42
# 68.395
# 68.39
# 67.64
# 66.2
# 66.3
# 65.3
# 65.7
# 64.9
# 64.6
# 64.3
# 63.9
# 64.6
# 63.5
# 62.1
# 59.3

#these values range from 0-25 plot them on a graph
# Path: src\main\java\FacialRecognision\temp.py

import matplotlib.pyplot as plt
import numpy as np

x = np.arange(0, 26)
y = [73.64, 73.64, 73.64, 73.64, 73.64, 72.84, 71.67, 71.26, 70.36, 68.42, 68.42, 68.395, 68.39, 67.64, 66.2, 66.3, 65.3, 65.7, 64.9, 64.6, 64.3, 63.9, 64.6, 63.5, 62.1, 59.3]

#fimd line of best fit
m, b = np.polyfit(x, y, 1)

#plot the line of best fit
plt.plot(x, m*x + b)


plt.plot(x, y)
plt.xlabel('percentage noise')
plt.ylabel('Accuracy')
plt.title('Accuracy vs Noise')
plt.show()

