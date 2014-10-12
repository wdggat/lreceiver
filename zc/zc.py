#!/usr/bin/python 

import copy

def get_all_buyarrs(buy_max, length, buy_min=1):
    buy_arr = [buy_min for i in range(0, length)]
    while not all([ b == buy_max for b in buy_arr]):
        yield copy.deepcopy(buy_arr)
	for i in range(1, length+1):
	    if buy_arr[0-i] == buy_max:
	        buy_arr[0-i] = buy_min
	    else:
	        buy_arr[0-i] += 1
		break

