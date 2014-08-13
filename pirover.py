
import socket, sys, time, initio, thread, threading, linesensor

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

def getCommand(message):
	return message[:-4]

def getValue(message):
	number = message[-4:]
	return number.lstrip("0")

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

# Receives input from socket and either assigns condition values,
# quits the program, sends a value from the ultrasonic sensor,
# or if it receives a command it appends the command to a list to be executed later
def getMessage() :
	while True :				
		try:
			message = clisock.recv(100)
			print message
		except socket.error:
			continue
		if (message.find('y') == 0) :
			global shouldCheck
			shouldCheck = True
			print 'shouldCheck is ' + str(shouldCheck)
			conditionDistance = message[1:4].lstrip("0")
			print 'conditionDistance = ' + conditionDistance
			global conditionCommand
			conditionCommand = message[4:-4]
			print 'conditionCommand = ' + conditionCommand
			global conditionValue
			conditionValue = message[-4:].lstrip("0")
			print 'conditionValue = ' + str(float(conditionValue))
		elif (message.find('n') == 0) :
			global shouldCheck
			shouldCheck = False
			print 'shouldCheck is ' + str(shouldCheck)
		elif (message == 'quit') :
			lock.acquire()
			clisock.close
			print 'Closing socket and creating new one...'
			createSocket()
			lock.release()
		elif (message == 'shutdown') :
			shutdown()
		elif (message == 'ultra') :
			clisock.send(str(initio.getDistance()) + '\n' )
			print 'Value sent!'
		elif (message == 'followline') :
			linesensor.followLine()
		elif (message == 'stop') :
			lock.acquire()
			initio.stop()
			commands[:] = ['none']
			values [:] = ['none']
			lock.release()
		else :
			lock.acquire()
			commands.append(getCommand(message))
			print commands
			values.append(getValue(message))		
			print values
			lock.release()
		time.sleep(0.1)

# Excecutes commands in the list, then moves them all along one and deletes the last
# command in the list

def doAction() :
	while True:		
		while (len(commands) != 0) :
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
			#print 'Finished action ' + commands[0] + values[0]
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
		if shouldCheck == True and initio.irAll() == True :
			lock.acquire()
			commands[:] = ['none']
			values[:] = ['0']
			doCondition()
			lock.release()
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

# Defines some variables and sets up GPIO pins
initio.init()
shouldCheck = False
speed = 60
commands = []
values = []


# Creates socket which waits for a connection
createSocket()

# Once connection received creates and starts both threads to listen for, store  and execute commands
lock = threading.Lock()
thread1 = inputThread()
thread2 = actionThread()
thread3 = obstacleThread()
thread1.start()
thread2.start()
thread3.start()
print 'Number of threads: ' + str(threading.active_count())
	
