	.data
a:
	31
	.text
main:
	load %x0, $a, %x3
	divi %x3, 2, %x4
	addi %x5, 2, %x5
loop:
	div %x3, %x5, %x11
	mul %x11, %x5, %x11
	sub %x3, %x11, %x11
	addi %x5, 1, %x5
	beq %x11, %x0, notprime
	bgt %x3, %x5, loop
	addi %x10, 1, %x10
	end
notprime:
	subi %10, 1, %x10
	end