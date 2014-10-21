#!/usr/bin/env python 

import utils
import spider_shengpingfu
from game import Game
import zc
from datetime import datetime
import notifier

#game: hostname, guestname, leaguename, starttime, concede1, concede2, sp ...
def main():
    games = [Game(item[0], item[1], item[2], item[3], item[4], item[5], item[6:9], item[9:12]) for item in spider_shengpingfu.spide()]
    for i in range(len(games) - 1):
        catched = []
        g1, g2 = games[i], games[i + 1]
	print "----new try --- %s ---" % datetime.now()
	print str(g1)
	print str(g2)
        for sp_arra in (g1.sp_arr1, g1.sp_arr2):
	    for sp_arrb in (g2.sp_arr1, g2.sp_arr2):
	        profits = zc.get_topN_profit_2s1(sp_arra, sp_arrb, 10, buy_max=7, buy_min=0)
		for p in profits:
		    print str(p)
		if len(profits) > 0:
		    c = "%s - %s\t%s\t%s - %s\t%s -- %s" % (g1.hostname,g1.guestname, str(sp_arra), g2.hostname, g2.guestname, str(sp_arrb), ' ** '.join([str(profit) for profit in profits]))
		    catched.append(c)
        if len(catched) > 0:
	    notifier.notify(catched)
	    print 'Mail send, len(catched): %d' % len(catched)
	    catched = []

if __name__ == '__main__':
    main()
