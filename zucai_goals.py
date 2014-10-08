#!/usr/bin/env python

import sys
    
BUY_MAX = 10

def _get_all_buyarrs(buy_max):
    for goal0 in range(buy_max):
        for goal1 in range(buy_max):
            for goal2 in range(buy_max):
                for goal3 in range(buy_max):
                    for goal4 in range(buy_max):
                        for goal5 in range(buy_max):
                            for goal6 in range(buy_max):
                                for goal7_plus in range(buy_max):
                                    yield [goal0, goal1, goal2, goal3, goal4, goal5, goal6, goal7_plus]

# len(sp_arr) == len(buy_arr)
def get_profit(sp_arr, buy_arr):
    profit_arr, c = [], _cost(buy_arr)
    for hit in range(len(sp_arr)):
        b = _bonus(sp_arr, buy_arr, hit)
        profit = b - c
        profit_arr.append(profit)
    return Profit(buy_arr, profit_arr)

def get_topN_profit(sp_arr, topN):
    if any([True for sp in sp_arr if sp <= 0]):
        print 'someone negative, drop.'
        return 

    profits, c = [], 0
    for buy_arr in _get_all_buyarrs(BUY_MAX):
        profit = get_profit(sp_arr, buy_arr)
	if not profit.all_positive():
	    continue
	else:
	    profits.append(profit)
	c += 1
	if c % 100000 == 0:
	    print 'c : %d' % c
    print 'all_positive.len = %d' % len(profits)
    sorted_profits = sorted(profits)
    return sorted_profits[0:topN]

def _bonus(sp_arr, buy_arr, hit):
    return sp_arr[hit] * buy_arr[hit] * 2
    
def _cost(buy_arr):
    return sum(buy_arr) * 2

class Profit():
    def __init__(self, buy_arr, profit_arr):
        self.buy_arr = buy_arr
        self.profit = profit_arr
        
    def all_positive(self):
        return all([p > 0 for p in self.profit])

    def __cmp__(self, other):
        cmp_sum = sum([cmp(self.profit[i], other.profit[i]) for i in range(len(self.profit))])
	return cmp_sum

    def __str__(self):
        #return "[%s]" % (", ".join(["%s: %s" % (self.buy_arr[i], self.profit[i]) for i in range(len(self.buy_arr))]))
	return "cost: %d, (%s) -- (%s)" % (2 * sum(self.buy_arr), ", ".join([str(i) for i in self.buy_arr]), ", ".join([str(i) for i in self.profit]))

#    def __getitem__(self, i):
#        return self.profit[i]

def main(sysargv):
    topN = 200
    sp_arr = [float(i) for i in sysargv]
    print 'SP: %s' % sp_arr
    profits = get_topN_profit(sp_arr, topN)
    for profit in profits:
        print str(profit)

if __name__=='__main__':
    main(sys.argv[1:])

