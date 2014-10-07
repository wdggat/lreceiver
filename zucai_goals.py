#!/usr/bin/env python

import sys
    
BUY_MAX = 10

def get_all_buyarrs(buy_max):
    for goal0 in [0..buy_max]:
        for goal1 in [0..buy_max]:
            for goal2 in [0..buy_max]:
                for goal3 in [0..buy_max]:
                    for goal4 in [0..buy_max]:
                        for goal5 in [0..buy_max]:
                            for goal6 in [0..buy_max]:
                                for goal7_plus in [0..buy_max]:
                                    yield [goal0, goal1, goal2, goal3, goal4, goal5, goal6, goal7_plus]

# len(sp_arr) == len(buy_arr)
def get_profit(sp_arr, buy_arr):
    profit_arr, c = [], _cost(buy_arr)
    for hit in range(len(sp_arr)):
        b = _bonus(sp_arr, buy_arr, hit)
        profit = b - c
        profit_arr.append(profit)
    return Profit(profit_arr)

def _bonus(sp_arr, buy_arr, hit):
    return sp_arr[hit] * buy_arr[hit]
    
def _cost(buy_arr):
    return sum(buy_arr) * 2

class Profit():
    def __init__(self, profit_arr):
        self.profit = profit_arr
        
    def all_positive(self):
        return all([p >= 0 for p in self.profit])

def main():
    pass

if __name__=='__name__':
    main()
