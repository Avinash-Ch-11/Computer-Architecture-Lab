	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	load %x0, $a, %x6
loop:
	divi %x3, 10, %x12
	muli %x12, 10, %12
	sub %x3, %x12, %x4
	muli %x5, 10, %x5
	add %x5, %x4, %x5
	divi %x3, 10, %x3
	bgt %x3, %x0, loop
	beq %x5, %x6, success
	subi %x10, 1, %x10
	end
success:
	addi %x10, 1, %x10
	end