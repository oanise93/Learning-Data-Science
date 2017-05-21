import math


def euclidean_distance(point1, point2):
    if len(point1) != len(point2):
        raise ValueError
    else:
        temp_sum = 0
        for i in range(0, len(point1)):
            temp_sum += (point1[i] - point2[i]) ** 2

        return math.sqrt(temp_sum)

test_point = [0, 0, 0]

observation1 = [0, 3, 0]
observation2 = [2, 0, 0]
observation3 = [0, 1, 3]
observation4 = [0, 1, 2]
observation5 = [-1, 0, 1]
observation6 = [1, 1, 1]

observations = [observation1, observation2, observation3, observation4, observation5, observation6]

if __name__ == "__main__":

    for observation in observations:
        print(euclidean_distance(test_point, observation), end=", ")
