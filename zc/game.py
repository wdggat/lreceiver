#!/usr/bin/env python 

import utils

# #game: hostname, guestname, leaguename, starttime, concede1, concede2, sp ...
class Game():
    def __init__(self, hostname, guestname, leaguename, starttime, concede1, concede2, sp_arr1, sp_arr2):
        self.hostname = hostname
	self.guestname = guestname
	self.leaguename = leaguename
	self.starttime = starttime
	self.concede1 = concede1
	self.concede2 = concede2
	self.sp_arr1 = sp_arr1
	self.sp_arr2 = sp_arr2

    def __str__(self):
        return "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s" % (self.hostname, self.guestname, self.leaguename, utils.make_datetime(self.starttime), self.concede1, self.concede2, self.sp_arr1, self.sp_arr2)
