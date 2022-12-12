	.data
n:
	5
	.text
main:
	load %x0, $n, %x10 
	addi %x0, 65535, %x2
	addi %x0, 1, %x9
	addi %x0, 0, %x5
	bgt %x10, %x9, a
	end
a:
	store %x5, 0, %x2
	subi %x2, 1, %x2
	addi %x0, 1, %x9
	bgt %x10, %x9, b
	end
b:
	addi %x0, 1, %x4
	store %x4, 0, %x2
	subi %x2, 1, %x2
	addi %x9, 1, %x9
	bgt %x10, %x9, c
	end
c:
	add %x3, %x4, %x5
	store %x5, 0, %x2
	addi %x4, 0, %x3
	addi %x5, 0, %x4
	subi %x2, 1, %x2
	addi %x9, 1, %x9
	bgt %x10, %x9, c
	end