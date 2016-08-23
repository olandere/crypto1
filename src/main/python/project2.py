from Crypto.Cipher import AES
import Crypto.Cipher.AES
import Crypto.Util.Counter
import struct 

key = bytearray.fromhex('140b41b22a29beb4061bda66b6747e14')
ct1 = bytearray.fromhex('4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81')
#iv = ct1[:16]
#cipher = AES.new(buffer(key), AES.MODE_CBC, buffer(iv))
#cipher.decrypt(buffer(ct1[16:]))

unpad = lambda s : s[0:-ord(s[-1])]

hexstr = lambda s: "".join(map(lambda b: format(b, "02x"), s))

def decrypt_cbc(key, message):
	iv = bytearray(message[:16])
	ct = message[16:]
	cipher = AES.new(buffer(key), AES.MODE_ECB)
	result = ''
	# split message into 16 byte blocks
	blocks = [ct[i:i+16] for i in range(0, len(ct), 16)]
	for b in blocks:
		ptb = cipher.decrypt(buffer(b))
		#xor with iv
		str = "".join([chr(x^y) for x, y in zip(bytearray(ptb), iv)])
		print 'str: ' + str
		iv = b
		result = result + str
	print 'result: ' + result
	return unpad(result)

def decrypt_ctr(key, message):
	print 'message length'
	print len(message)
	iv = bytearray(message[:8]+'00000000')
	#iv = bytearray(message[:16])
	ivl = long(hexstr(iv), 16)
	#print 'ivl: '+ivl
	ct = message[8:]	
	print "Length: " 
	print len(ct)
	cipher = AES.new(buffer(key), AES.MODE_ECB)
	result = ''
	# split message into 16 byte blocks
	blocks = [ct[i:i+16] for i in range(0, len(ct), 16)]
	for b in blocks:
		ptb = cipher.decrypt(buffer(b))
		str = "".join([chr(x^y) for x, y in zip(bytearray(ptb), iv)])
		print 'str: ' + str
		ivl += 1
		iv = bytearray.fromhex(hex(ivl)[2:][:-1])
		result = result + str
	print 'result: ' + result
	return unpad(result)

def decrypt_ctr2(key, message):	
	iv = bytearray(message[:16])
	ctr = Crypto.Util.Counter.new(128, initial_value=long(hexstr(iv), 16))
	cipher = Crypto.Cipher.AES.new(buffer(key), Crypto.Cipher.AES.MODE_CTR, counter=ctr)
	return cipher.decrypt(buffer(message[16:]))
	

key2 = bytearray.fromhex('140b41b22a29beb4061bda66b6747e14')
ct2 = bytearray.fromhex('5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253')
print decrypt_cbc(key, ct1)
print decrypt_cbc(key2, ct2)


key3 = bytearray.fromhex('36f18357be4dbd77f050515c73fcf9f2')
ct3 = bytearray.fromhex('69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329')
print decrypt_ctr2(key3, ct3)

key4 = bytearray.fromhex('36f18357be4dbd77f050515c73fcf9f2')
ct4 = bytearray.fromhex('770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451')
print decrypt_ctr2(key4, ct4)
