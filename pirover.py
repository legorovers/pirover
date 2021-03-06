import socket, sys, time, initio, thread, threading, linesensor, servo

# Shuts down the Raspberry Pi
def shutdown():
	command = "/usr/bin/sudo /sbin/shutdown -h now"
	import subprocess
	process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
	output = process.communicate()[0]
	print output

# Creates socket which waits for a connection
def createSocket():
	srvsock = socket.socket()
	srvsock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	srvsock.bind( ('', 10001) )
	srvsock.listen( 5 )
	print 'Waiting for connection...'
	global clisock
	clisock, (remhost, remport) = srvsock.accept()
	clisock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	print 'Connection successful!'

# Sends ultrasonic sensor distance every second
def sendUltra():
	while True:
		if shouldUltra == True:
			clisock.send(str(initio.getDistance()) + '\n' )
			print 'Value sent!'
			time.sleep(2)

# Gets command part of message e.g. forward	
def getCommand(message):
	return message[:-4]

# Gets value part of message e.g. 2 seconds
def getValue(message):
	number = message[-4:]
	return number.lstrip("0")

# Does action specified by conditionCommand. Is called when obstacle
# sensors are activated
def doCondition() :
	if conditionCommand == 'reverse' :
		initio.reverse(speed)
		time.sleep(float(conditionValue))
		initio.stop()
	elif conditionCommand == 'stop' :
		initio.stop()
	elif conditionCommand == 'left' :
		initio.spinLeft(100)
		time.sleep(float(conditionValue))
		initio.stop()
	elif conditionCommand == 'right' :
		initio.spinRight(100)
		time.sleep(float(conditionValue))
		initio.stop()
	elif conditionCommand == 'reverseleft' :
		initio.turnReverse(5,100)
		time.sleep(float(conditionValue))
		initio.stop()
	elif conditionCommand == 'reverseright' :
		initio.turnReverse(100,5)
		time.sleep(float(conditionValue))
		initio.stop()
	global shouldCheck
	shouldCheck = False
	initio.stop()

# Receives input from socket and does appropriate action
def getMessage() :
	while True :				
		try:
			global message
			message = clisock.recv(100)
			print message
		except socket.error:
			continue
# If condition box is ticked, variables are assigned so robot does an action
# if it encounters an obstacle
		if (message.find('y') == 0) :
			global shouldCheck
			shouldCheck = True
			print 'shouldCheck is ' + str(shouldCheck)
			global conditionDistance
			conditionDistance = message[1:4].lstrip("0")
			print 'conditionDistance = ' + conditionDistance
			global conditionCommand
			conditionCommand = message[4:-4]
			print 'conditionCommand = ' + conditionCommand
			global conditionValue
			conditionValue = message[-4:].lstrip("0")
			print 'conditionValue = ' + str(float(conditionValue))
# Condition box is not ticked, so obstacles are not checked for
		elif (message.find('n') == 0) :
			global shouldCheck
			shouldCheck = False
			print 'shouldCheck is ' + str(shouldCheck)
# If Java program is closed, this program closes the socket and waits for
# another connection
		elif (message == 'quit') :
			lock.acquire()
			clisock.close()
			print 'Closing socket and creating new one...'
			createSocket()
			lock.release()
# Shuts down the raspberry pi
		elif (message == 'shutdown') :
			clisock.close()
			shutdown()
# Starts sending the ultrasonic sensor distance every 2 seconds. If the
# distance is already being sent, it stops sending it
		elif (message == 'ultra') :
			if shouldUltra == True:
				global shouldUltra
				print 'changing shouldUltra to False'
				shouldUltra = False
			else :
				global shouldUltra
				print 'changing shouldUltra to True'
				shouldUltra = True 
# Makes the robot follow a line
		elif (message == 'followline') :
			linesensor.followLine()
# Stops the robot and clears commands from the queue
		elif (message == 'stop') :
			lock.acquire()
			initio.stop()
			commands[:] = ['none']
			values [:] = ['none']
			lock.release()
# Sets the servo position back to the middle
		elif (message == 'servoinit') :
			servo.panInit()
			servo.tiltInit()
# Moves the servos
		elif (message == 'panleft') :
			servo.panLeft()
		elif (message == 'panright') :
			servo.panRight()
		elif (message == 'tiltup') :
			servo.tiltUp()
		elif (message == 'tiltdown') :
			servo.tiltDown()
# If anything else is sent, it must be a command so it is appended to a list
# to be executed
		else :
			lock.acquire()
			commands.append(getCommand(message))
			print commands
			values.append(getValue(message))		
			print values
			lock.release()
		time.sleep(0.1)

# Excecutes commands in the list, then moves them all along one and deletes the
# last command in the list

def doAction() :
	while True:		
		while (len(commands) != 0 and len(values) != 0) :
			if commands[0] == 'forward' :
				initio.forward(speed)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'reverse' :
				initio.reverse(speed)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'left' :
				initio.spinLeft(100)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'right' :
				initio.spinRight(100)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'forwardleft' :
				initio.turnForward(5,100)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'forwardright' :
				initio.turnForward(100,5)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'reverseright' :
				initio.turnReverse(100,5)
				time.sleep(float(values[0]))
				initio.stop()
			elif commands[0] == 'reverseleft' :
				initio.turnReverse(5,100)
				time.sleep(float(values[0]))
				initio.stop()
			lock.acquire()
			if (len(commands) > 1) :
				for i in range(1, (len(commands))) :
					commands[i-1] = commands[i]
					values [i-1] = values[i]
			values.pop()
			commands.pop()
			lock.release()

# This checks if an obstacle is detected and stops the robot if true
def checkObstacle() :
	while True:
		if shouldCheck == True:
			if conditionDistance == '' and initio.irAll() == True:
				lock.acquire()
				commands[:] = ['none']
				values[:] = ['0']
				doCondition()
				lock.release()
			elif conditionDistance != '' and initio.getDistance() < int(conditionDistance) :	
				lock.acquire()
				commands[:] = ['none']
				values[:] = ['0']
				doCondition()
				lock.release()
			time.sleep(0.1)		
		time.sleep(0.1)


# Creates thread class to receive input
class inputThread(threading.Thread):
	def _init_(self):
		threading.Thread._init_(self)
	def run(self):
		getMessage()
# Creates thread class to execute commands
class actionThread(threading.Thread):
	def _init_(self) :
		threading.Thread._init_(self)
	def run(self) :
		doAction()

# Creates thread to detect and avoid obstacles
class obstacleThread(threading.Thread) :
	def _init_(self) :
		threading.Thread._init_(self)
	def run(self) :
		checkObstacle()

# Creates thread to send ultrasonic sensor distance every 2 seconds
class ultraThread(threading.Thread) :
	def _init_(self) :
		threading.Thread._init_(self)
	def run(self) :
		sendUltra()

# Defines some variables and sets up GPIO pins
initio.init()
shouldCheck = False
shouldUltra = False
speed = 60
commands = []
values = []


# Creates socket which waits for a connection
createSocket()

# Once connection received creates and starts all threads to listen for, 
# store  and execute commands
lock = threading.Lock()
thread1 = inputThread()
thread2 = actionThread()
thread3 = obstacleThread()
thread4 = ultraThread()

thread1.start()
thread2.start()
thread3.start()
thread4.start()
print 'Number of threads: ' + str(threading.active_count())
	
