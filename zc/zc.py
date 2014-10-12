#!/usr/bin/python 

import copy
from datetime import datetime
from profit import Profit

BUY_MAX=8

def _get_all_buyarrs(buy_max, length, buy_min=0):
    buy_arr = [buy_min for i in range(0, length)]
    while not all([ b == buy_max for b in buy_arr]):
        yield copy.deepcopy(buy_arr)
	for i in range(1, length+1):
	    if buy_arr[0-i] == buy_max:
	        buy_arr[0-i] = buy_min
	    else:
	        buy_arr[0-i] += 1
		break

def get_topN_profit(sp_arr, topN, profit_multiple=2, buy_max=BUY_MAX):
    if any([True for sp in sp_arr if sp <= 0]):
        print 'someone negative, drop.'
        return []

#    s, legal = sp_checker.check(sp_arr)
#    print '%s sp_check.value: %f' % (sp_arr, s)
#    if not legal:
#        print 'sp useless, drop'
#	return []

    profits, c = [], 0
    for buy_arr in _get_all_buyarrs(buy_max, len(sp_arr)):
	c += 1
	if c % 10000000 == 0:
	    print 'c : %d, %s' % (c, datetime.now())
        profit = Profit.get_from_sparr(sp_arr, buy_arr, multiple=profit_multiple)
	if not profit.qualified():
	    continue
	else:
	    profits.append(profit)
    print 'qualified.len = %d' % len(profits)
    sorted_profits = sorted(profits, reverse=True)
    return sorted_profits[0:topN]

def get_topN_profit_2s1(sp_arra, sp_arrb, topN, buy_max=BUY_MAX):
    sp_arr = []
    for sp_a in sp_arra:
        for sp_b in sp_arrb:
	    sp_arr.append(sp_a * sp_b)

    print 'sp_a: %s, sp_b: %s' % (sp_arra, sp_arrb)
    return get_topN_profit(sp_arr, topN, buy_max=buy_max)
    
