#!/usr/bin/env python 
#-*-encoding: utf-8 -*-

import sys 

class Profit():
    def __init__(self, buy_arr, profit_arr):
        self.buy_arr = buy_arr
        self.profit = profit_arr
	self.cost = _cost(buy_arr)
        
    def all_positive(self):
        return all([p > 0 for p in self.profit])

    def qualified(self):
        neg = 0
	for p in self.profit:
	    if p < 5:
	        neg += 1
	return neg <= 1

    def __cmp__(self, other):
	return self.weight() - other.weight()

    def __str__(self):
        #return "[%s]" % (", ".join(["%s: %s" % (self.buy_arr[i], self.profit[i]) for i in range(len(self.buy_arr))]))
	return "cost: %d, (%s) -- (%s)" % (_cost(self.buy_arr), ", ".join([str(i) for i in self.buy_arr]), ", ".join([str(i) for i in self.profit]))

    def weight(self):
	#return (sum(self.profit) - max(self.profit) - min(self.profit)) / self.cost
	return sum(self.profit) 

#    def __getitem__(self, i):
#        return self.profit[i]

    # len(sp_arr) == len(buy_arr)
    @classmethod
    def get_from_sparr(self, sp_arr, buy_arr, multiple=2):
        profit_arr, c = [], _cost(buy_arr)
        for hit in range(len(sp_arr)):
            b = _bonus(sp_arr, buy_arr, hit, multiple)
            profit = b - c
            profit_arr.append(profit)
        return Profit(buy_arr, profit_arr)

def _bonus(sp_arr, buy_arr, hit, multiple=2):
    return sp_arr[hit] * buy_arr[hit] * multiple

def _cost(buy_arr):
    return sum(buy_arr) * 2

