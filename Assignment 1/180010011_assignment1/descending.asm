    .data
a:
	5000
	0
	123
	0
	500
	311
n:
    6
    .text
main:
	load %x0, $a, %10
    load %x0, $n, %x3
	subi %x3, 2, %x13 
    addi %x0, 0, %x6
    subi %x0, 1, %x5
loopx:
	addi %x5, 1, %x5
    load %x5, $a, %x4
    bgt %x4, %x2, up
    bgt %x3, %x5, loopx
    beq %x3, %x5, loop
loop:
    bgt %x3, %x5, loopx
    load %x6, $a, %x8
    store %x2, $a, %x6
    store %x8, $a, %x7
    addi %x6, 1, %x6
    subi %x6, 1, %x5
	addi %x0, 0, %x2
	bgt %x13, %x6, loop
    end
up:
    addi %x4, 0, %x2
    addi %x5, 0, %x7
    jmp loopx