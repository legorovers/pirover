import RPi.GPIO as GPIO
import time

pan = 22
tilt = 18

GPIO.setmode(GPIO.BOARD)

GPIO.setup(pan, GPIO.OUT)
GPIO.output(pan, True)

GPIO.setup(tilt, GPIO.OUT)
GPIO.output(tilt, True)

global prevPan, prevTilt
prevPan = 1.2
prevTilt = 1.25

def panInit() :
	print 'Panning to centre'
	for i in range(1,25):
		GPIO.output(22,True)
		time.sleep(0.0012)
		GPIO.output(22,False)
		time.sleep(0.02)
	global prevPan
	prevPan = 1.2

def tiltInit() :
	print 'Tilting to centre'
	for i in range(1,25):
		GPIO.output(18,True)
		time.sleep(0.00125)
		GPIO.output(18,False)
		time.sleep(0.02)
	global prevTilt
	prevTilt = 1.25
	
def panLeft():
	try :
		pulse = prevPan + 0.2
		end = 6 + 14*(abs(pulse-prevPan))
		for i in range (1,int(end)):
			GPIO.output(22, True)
			seconds = pulse / 1000
			time.sleep(seconds)
			GPIO.output(22,False)
			time.sleep(0.02)
		global prevPan
		prevPan = pulse
	except IOError:
		pass

def tiltUp():
	try:
		pulse = prevTilt - 0.2
		end = 6 + 14*(abs(pulse-prevTilt))
		for i in range (1,int(end)):
			GPIO.output(18, True)
			seconds = pulse / 1000
			time.sleep(seconds)
			GPIO.output(18,False)
			time.sleep(0.02)
		global prevTilt
		prevTilt = pulse
	except IOError:
		pass

def panRight():
	try:
		pulse = prevPan - 0.2
		end = 6 + 14*(abs(pulse-prevPan))
		for i in range (1,int(end)):
			GPIO.output(22, True)
			seconds = pulse / 1000
			time.sleep(seconds)
			GPIO.output(22, False)
			time.sleep(0.02)
		global prevPan
		prevPan = pulse
	except IOError:
		pass

def tiltDown():
	try:
		pulse = prevTilt + 0.2
		end = 6 + 14*(abs(pulse-prevTilt))
		for i in range (1,int(end)):
			GPIO.output(18, True)
			seconds = pulse / 1000
			time.sleep(seconds)
			GPIO.output(18, False)
			time.sleep(0.02)
		global prevTilt
		prevTilt = pulse
	except IOError:
		pass
		
