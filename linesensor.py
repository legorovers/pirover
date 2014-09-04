import initio, time

speed =60
speedLow = 5
speedHigh = 100
global shouldFollow
shouldFollow = True
initio.init()


# Follows a curved line (not very well)
def followLine() :
	mostRecent = 'none'
	counter = 0
	
	if initio.irLeftLine() == True and initio.irRightLine() == True :
		while shouldFollow == True :
			while initio.irLeftLine() == True and initio.irRightLine() == True :
				counter = 0
				mostRecent = 'forward'
				initio.forward(speed)
			while  initio.irLeftLine() == True and initio.irRightLine() == False :
				counter = 0
				mostRecent = 'left'
				#initio.turnForward(speedLow,speedHigh)
				initio.spinLeft(100)
			while initio.irLeftLine() == False and initio.irRightLine() == True :
				counter = 0
				mostRecent = 'right'
				#initio.turnForward(speedHigh,speedLow)
				initio.spinRight(100)
			if initio.irLeftLine() == False and initio.irRightLine() == False :
				initio.stop()
				break 

# This part makes robot continue doing its last action for a second or so, to 
# try and find the line again if it has been lost. Optional.
#			while initio.irLeftLine() == False and initio.irRightLine() == False : 	
#				counter = counter + 1
#				if counter > 6 :
#					initio.stop()
#					return
#				elif mostRecent == 'forward' :
#					initio.forward(speed)
#					time.sleep(0.1)
#				elif mostRecent == 'left' :
#					initio.turnForward(speedLow,speedHigh)
#					time.sleep(0.2)
#				elif mostRecent == 'right' :
#					initio.turnForward(speedHigh,speedLow)
#					time.sleep(0.2)
#		initio.stop()
#
	elif initio.irLeftLine() == False and initio.irRightLine() == False :
		while shouldFollow == True:
			while initio.irLeftLine() == False and initio.irRightLine() == False :
				counter = 0
				mostRecent = 'forward'
				initio.forward(speed)
			while  initio.irLeftLine() == False and initio.irRightLine() == True :
				counter = 0
				mostRecent = 'left'
				#initio.turnForward(speedLow,speedHigh)
				initio.spinLeft(100)
			while initio.irLeftLine() == True and initio.irRightLine() == False :
				counter = 0
				mostRecent = 'right'
				initio.spinRight(100)
				#initio.turnForward(speedHigh,speedLow)
			if initio.irLeftLine() == True and initio.irRightLine() == True :
				initio.stop()
				break 	

# This part makes robot continue doing its last action for a second or so, to 
# try and find the line again if it has been lost. Optional.
#			while initio.irLeftLine() == True and initio.irRightLine() == True : 	
#				counter = counter + 1
#				if counter > 6 :
#					initio.stop()
#					return
#				elif mostRecent == 'forward' :
#					initio.forward(speed)
#					time.sleep(0.1)
#				elif mostRecent == 'left' :
#					initio.turnForward(speedLow,speedHigh)
#					time.sleep(0.2)
#				elif mostRecent == 'right' :
#					initio.turnForward(speedHigh,speedLow)
#					time.sleep(0.2)
#		initio.stop()


# This is supposed to follow the edge of the line but doesnt work so well.
#	while shouldFollow == True:
#		initio.forward(speed)
#	
#		if initio.irLeftLine() == True and initio.irRightLine() == False:
#			while initio.irLeftLine() == True and initio.irRightLine() == False:
#				initio.forward(speed)
#			while initio.irLeftLine() == True and initio.irRightLine() == True:
#				#initio.spinRight(100)
#				initio.turnForward(100,5)
#			while initio.irLeftLine() == False and initio.irRightLine() == False:
				#initio.spinLeft(100)
#				initio.turnForward(5,100)
#		
#		if initio.irLeftLine() == False and initio.irRightLine() == True:
#			while initio.irLeftLine() == False and initio.irRightLine() == True:
#				initio.forward(speed)
#			while initio.irLeftLine() == True and initio.irRightLine() == True:
#				#initio.spinLeft(100)
#				initio.turnForward(5,100)
#			while initio.irLeftLine() == False and initio.irRightLine() == False:
#				#initio.spinRight(100)
#				initio.turnForward(100,5)
#	else :
#		initio.stop()
#		return
