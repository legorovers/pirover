import initio, time

speed =60
speedLow = 5
speedHigh = 100
initio.init()


# The robot follows a straight line
#while initio.irLeftLine() == True or initio.irRightLine() == True :
	#initio.forward(speed)
#else : 
	#initio.cleanup

# Follows a curved line (not very well)
def followLine() :
	mostRecent = 'none'
	counter = 0
	
	if initio.irLeftLine() == True and initio.irRightLine() == True :
		while True:
			while initio.irLeftLine() == True and initio.irRightLine() == True :
				counter = 0
				mostRecent = 'forward'
				initio.forward(speed)
			while  initio.irLeftLine() == True and initio.irRightLine() == False :
				counter = 0
				mostRecent = 'left'
				initio.turnForward(speedLow,speedHigh)
			while initio.irLeftLine() == False and initio.irRightLine() == True :
				counter = 0
				mostRecent = 'right'
				initio.turnForward(speedHigh,speedLow)
			while initio.irLeftLine() == False and initio.irRightLine() == False : 	
				counter = counter + 1
				if counter > 6 :
					initio.stop()
					return
				elif mostRecent == 'forward' :
					initio.forward(speed)
					time.sleep(0.1)
				elif mostRecent == 'left' :
					initio.turnForward(speedLow,speedHigh)
					time.sleep(0.2)
				elif mostRecent == 'right' :
					initio.turnForward(speedHigh,speedLow)
					time.sleep(0.2)
				initio.stop()

	if initio.irLeftLine() == False and initio.irRightLine() == False :
		while True:
			while initio.irLeftLine() == False and initio.irRightLine() == False :
				counter = 0
				mostRecent = 'forward'
				initio.forward(speed)
			while  initio.irLeftLine() == False and initio.irRightLine() == True :
				counter = 0
				mostRecent = 'left'
				initio.turnForward(speedLow,speedHigh)
			while initio.irLeftLine() == True and initio.irRightLine() == False :
				counter = 0
				mostRecent = 'right'
				initio.turnForward(speedHigh,speedLow)
			while initio.irLeftLine() == True and initio.irRightLine() == True : 	
				counter = counter + 1
				if counter > 6 :
					initio.stop()
					return
				elif mostRecent == 'forward' :
					initio.forward(speed)
					time.sleep(0.1)
				elif mostRecent == 'left' :
					initio.turnForward(speedLow,speedHigh)
					time.sleep(0.2)
				elif mostRecent == 'right' :
					initio.turnForward(speedHigh,speedLow)
					time.sleep(0.2)
				initio.stop()
