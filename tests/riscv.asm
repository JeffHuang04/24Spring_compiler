  .data
x:
  .word 1

  .data
y:
  .word 2

  .data
z:
  .word 3

  .data
a:
  .word 4

  .data
b:
  .word 5

  .data
c:
  .word 6

  .data
d:
  .word 7

  .data
e:
  .word 8

  .data
f:
  .word 9

  .data
g:
  .word 10

  .data
h:
  .word 11

  .data
i:
  .word 12

  .data
j:
  .word 13

  .data
k:
  .word 14

  .data
l:
  .word 15

  .data
m:
  .word 16

  .data
n:
  .word 17

  .data
o:
  .word 18

  .data
p:
  .word 19

  .data
q:
  .word 20

  .text
  .globl main
main:
  addi sp, sp, 0
mainEntry:
  li t0, 1
  mv s0, t0
  li t0, 2
  mv s1, t0
  li t0, 3
  mv s2, t0
  li t0, 4
  mv s3, t0
  li t0, 5
  mv s4, t0
  li t0, 6
  mv s5, t0
  li t0, 7
  mv s6, t0
  li t0, 8
  mv s7, t0
  li t0, 9
  mv s8, t0
  li t0, 10
  mv s9, t0
  li t0, 11
  mv s10, t0
  li t0, 12
  mv s11, t0
  li t0, 13
  mv a0, t0
  li t0, 14
  mv a1, t0
  li t0, 15
  mv a2, t0
  li t0, 16
  mv a3, t0
  li t0, 17
  mv a4, t0
  li t0, 18
  mv a5, t0
  li t0, 19
  mv a6, t0
  li t0, 20
  mv a7, t0
  la t0, x
  lw t0, 0(t0)
  mv t2, t0
  mv t0, t2
  li t1, 1
  add t0, t0, t1
  mv t3, t0
  la t1, x
  sw t3, 0(t1)
  la t0, y
  lw t0, 0(t0)
  mv t4, t0
  mv t0, t4
  li t1, 2
  add t0, t0, t1
  mv t5, t0
  la t1, y
  sw t5, 0(t1)
  la t0, z
  lw t0, 0(t0)
  mv t6, t0
  mv t0, t6
  li t1, 3
  add t0, t0, t1
  mv t2, t0
  la t1, z
  sw t2, 0(t1)
  la t0, a
  lw t0, 0(t0)
  mv t3, t0
  mv t0, t3
  li t1, 4
  add t0, t0, t1
  mv t4, t0
  la t1, a
  sw t4, 0(t1)
  la t0, b
  lw t0, 0(t0)
  mv t5, t0
  mv t0, t5
  li t1, 5
  add t0, t0, t1
  mv t6, t0
  la t1, b
  sw t6, 0(t1)
  la t0, c
  lw t0, 0(t0)
  mv t2, t0
  mv t0, t2
  li t1, 6
  add t0, t0, t1
  mv t3, t0
  la t1, c
  sw t3, 0(t1)
  la t0, d
  lw t0, 0(t0)
  mv t4, t0
  mv t0, t4
  li t1, 7
  add t0, t0, t1
  mv t5, t0
  la t1, d
  sw t5, 0(t1)
  la t0, e
  lw t0, 0(t0)
  mv t6, t0
  mv t0, t6
  li t1, 8
  add t0, t0, t1
  mv t2, t0
  la t1, e
  sw t2, 0(t1)
  la t0, f
  lw t0, 0(t0)
  mv t3, t0
  mv t0, t3
  li t1, 9
  add t0, t0, t1
  mv t4, t0
  la t1, f
  sw t4, 0(t1)
  la t0, g
  lw t0, 0(t0)
  mv t5, t0
  mv t0, t5
  li t1, 10
  add t0, t0, t1
  mv t6, t0
  la t1, g
  sw t6, 0(t1)
  la t0, h
  lw t0, 0(t0)
  mv t2, t0
  mv t0, t2
  li t1, 11
  add t0, t0, t1
  mv t3, t0
  la t1, h
  sw t3, 0(t1)
  la t0, i
  lw t0, 0(t0)
  mv t4, t0
  mv t0, t4
  li t1, 12
  add t0, t0, t1
  mv t5, t0
  la t1, i
  sw t5, 0(t1)
  la t0, j
  lw t0, 0(t0)
  mv t6, t0
  mv t0, t6
  li t1, 13
  add t0, t0, t1
  mv t2, t0
  la t1, j
  sw t2, 0(t1)
  la t0, k
  lw t0, 0(t0)
  mv t3, t0
  mv t0, t3
  li t1, 14
  add t0, t0, t1
  mv t4, t0
  la t1, k
  sw t4, 0(t1)
  la t0, l
  lw t0, 0(t0)
  mv t5, t0
  mv t0, t5
  li t1, 15
  add t0, t0, t1
  mv t6, t0
  la t1, l
  sw t6, 0(t1)
  la t0, m
  lw t0, 0(t0)
  mv t2, t0
  mv t0, t2
  li t1, 16
  add t0, t0, t1
  mv t3, t0
  la t1, m
  sw t3, 0(t1)
  la t0, n
  lw t0, 0(t0)
  mv t4, t0
  mv t0, t4
  li t1, 17
  add t0, t0, t1
  mv t5, t0
  la t1, n
  sw t5, 0(t1)
  la t0, o
  lw t0, 0(t0)
  mv t6, t0
  mv t0, t6
  li t1, 18
  add t0, t0, t1
  mv t2, t0
  la t1, o
  sw t2, 0(t1)
  la t0, p
  lw t0, 0(t0)
  mv t3, t0
  mv t0, t3
  li t1, 19
  add t0, t0, t1
  mv t4, t0
  la t1, p
  sw t4, 0(t1)
  la t0, q
  lw t0, 0(t0)
  mv t5, t0
  mv t0, t5
  li t1, 20
  add t0, t0, t1
  mv t6, t0
  la t1, q
  sw t6, 0(t1)
  mv t2, s0
  mv t0, t2
  li t1, 2
  mul t0, t0, t1
  mv t3, t0
  mv s0, t3
  mv t4, s1
  mv t0, t4
  li t1, 2
  mul t0, t0, t1
  mv t5, t0
  mv s1, t5
  mv t6, s2
  mv t0, t6
  li t1, 2
  mul t0, t0, t1
  mv t2, t0
  mv s2, t2
  mv t3, s3
  mv t0, t3
  li t1, 2
  mul t0, t0, t1
  mv t4, t0
  mv s3, t4
  mv t5, s4
  mv t0, t5
  li t1, 2
  mul t0, t0, t1
  mv t6, t0
  mv s4, t6
  mv t2, s5
  mv t0, t2
  li t1, 2
  mul t0, t0, t1
  mv t3, t0
  mv s5, t3
  mv t4, s6
  mv t0, t4
  li t1, 2
  mul t0, t0, t1
  mv t5, t0
  mv s6, t5
  mv t6, s7
  mv t0, t6
  li t1, 2
  mul t0, t0, t1
  mv t2, t0
  mv s7, t2
  mv t3, s8
  mv t0, t3
  li t1, 2
  mul t0, t0, t1
  mv t4, t0
  mv s8, t4
  mv t5, s9
  mv t0, t5
  li t1, 2
  mul t0, t0, t1
  mv t6, t0
  mv s9, t6
  mv t2, s10
  mv t0, t2
  li t1, 2
  mul t0, t0, t1
  mv t3, t0
  mv s10, t3
  mv t4, s11
  mv t0, t4
  li t1, 2
  mul t0, t0, t1
  mv t5, t0
  mv s11, t5
  mv t6, a0
  mv t0, t6
  li t1, 2
  mul t0, t0, t1
  mv t2, t0
  mv a0, t2
  mv t3, a1
  mv t0, t3
  li t1, 2
  mul t0, t0, t1
  mv t4, t0
  mv a1, t4
  mv t5, a2
  mv t0, t5
  li t1, 2
  mul t0, t0, t1
  mv t6, t0
  mv a2, t6
  mv t2, a3
  mv t0, t2
  li t1, 2
  mul t0, t0, t1
  mv t3, t0
  mv a3, t3
  mv t4, a4
  mv t0, t4
  li t1, 2
  mul t0, t0, t1
  mv t5, t0
  mv a4, t5
  mv t6, a5
  mv t0, t6
  li t1, 2
  mul t0, t0, t1
  mv t2, t0
  mv a5, t2
  mv t3, a6
  mv t0, t3
  li t1, 2
  mul t0, t0, t1
  mv t4, t0
  mv a6, t4
  mv t5, a7
  mv t0, t5
  li t1, 2
  mul t0, t0, t1
  mv t6, t0
  mv a7, t6
  mv t2, s0
  mv t3, s1
  mv t0, t2
  mv t1, t3
  add t0, t0, t1
  mv t4, t0
  mv t5, s2
  mv t0, t4
  mv t1, t5
  add t0, t0, t1
  mv t6, t0
  mv s0, s3
  mv t0, t6
  mv t1, s0
  add t0, t0, t1
  mv s1, t0
  mv t3, s4
  mv t0, s1
  mv t1, t3
  add t0, t0, t1
  mv t2, t0
  mv s2, s5
  mv t0, t2
  mv t1, s2
  add t0, t0, t1
  mv t5, t0
  mv t4, s6
  mv t0, t5
  mv t1, t4
  add t0, t0, t1
  mv s3, t0
  mv s0, s7
  mv t0, s3
  mv t1, s0
  add t0, t0, t1
  mv t6, t0
  mv s4, s8
  mv t0, t6
  mv t1, s4
  add t0, t0, t1
  mv t3, t0
  mv s1, s9
  mv t0, t3
  mv t1, s1
  add t0, t0, t1
  mv s5, t0
  mv s2, s10
  mv t0, s5
  mv t1, s2
  add t0, t0, t1
  mv t2, t0
  mv s6, s11
  mv t0, t2
  mv t1, s6
  add t0, t0, t1
  mv t4, t0
  mv t5, a0
  mv t0, t4
  mv t1, t5
  add t0, t0, t1
  mv s7, t0
  mv s0, a1
  mv t0, s7
  mv t1, s0
  add t0, t0, t1
  mv s3, t0
  mv s8, a2
  mv t0, s3
  mv t1, s8
  add t0, t0, t1
  mv s4, t0
  mv t6, a3
  mv t0, s4
  mv t1, t6
  add t0, t0, t1
  mv s9, t0
  mv s1, a4
  mv t0, s9
  mv t1, s1
  add t0, t0, t1
  mv t3, t0
  mv s10, a5
  mv t0, t3
  mv t1, s10
  add t0, t0, t1
  mv s2, t0
  mv s5, a6
  mv t0, s2
  mv t1, s5
  add t0, t0, t1
  mv s11, t0
  mv s6, a7
  mv t0, s11
  mv t1, s6
  add t0, t0, t1
  mv t2, t0
  la t0, x
  lw t0, 0(t0)
  mv a0, t0
  mv t0, t2
  mv t1, a0
  add t0, t0, t1
  mv t5, t0
  la t0, y
  lw t0, 0(t0)
  mv t4, t0
  mv t0, t5
  mv t1, t4
  add t0, t0, t1
  mv a1, t0
  la t0, z
  lw t0, 0(t0)
  mv s0, t0
  mv t0, a1
  mv t1, s0
  add t0, t0, t1
  mv s7, t0
  la t0, a
  lw t0, 0(t0)
  mv a2, t0
  mv t0, s7
  mv t1, a2
  add t0, t0, t1
  mv s8, t0
  la t0, b
  lw t0, 0(t0)
  mv s3, t0
  mv t0, s8
  mv t1, s3
  add t0, t0, t1
  mv a3, t0
  la t0, c
  lw t0, 0(t0)
  mv t6, t0
  mv t0, a3
  mv t1, t6
  add t0, t0, t1
  mv s4, t0
  la t0, d
  lw t0, 0(t0)
  mv a4, t0
  mv t0, s4
  mv t1, a4
  add t0, t0, t1
  mv s1, t0
  la t0, e
  lw t0, 0(t0)
  mv s9, t0
  mv t0, s1
  mv t1, s9
  add t0, t0, t1
  mv a5, t0
  la t0, f
  lw t0, 0(t0)
  mv s10, t0
  mv t0, a5
  mv t1, s10
  add t0, t0, t1
  mv t3, t0
  la t0, g
  lw t0, 0(t0)
  mv a6, t0
  mv t0, t3
  mv t1, a6
  add t0, t0, t1
  mv s5, t0
  la t0, h
  lw t0, 0(t0)
  mv s2, t0
  mv t0, s5
  mv t1, s2
  add t0, t0, t1
  mv a7, t0
  la t0, i
  lw t0, 0(t0)
  mv s6, t0
  mv t0, a7
  mv t1, s6
  add t0, t0, t1
  mv s11, t0
  la t0, j
  lw t0, 0(t0)
  mv a0, t0
  mv t0, s11
  mv t1, a0
  add t0, t0, t1
  mv t2, t0
  la t0, k
  lw t0, 0(t0)
  mv t4, t0
  mv t0, t2
  mv t1, t4
  add t0, t0, t1
  mv t5, t0
  la t0, l
  lw t0, 0(t0)
  mv s0, t0
  mv t0, t5
  mv t1, s0
  add t0, t0, t1
  mv a1, t0
  la t0, m
  lw t0, 0(t0)
  mv a2, t0
  mv t0, a1
  mv t1, a2
  add t0, t0, t1
  mv s7, t0
  la t0, n
  lw t0, 0(t0)
  mv s3, t0
  mv t0, s7
  mv t1, s3
  add t0, t0, t1
  mv s8, t0
  la t0, o
  lw t0, 0(t0)
  mv t6, t0
  mv t0, s8
  mv t1, t6
  add t0, t0, t1
  mv a3, t0
  la t0, p
  lw t0, 0(t0)
  mv a4, t0
  mv t0, a3
  mv t1, a4
  add t0, t0, t1
  mv s4, t0
  la t0, q
  lw t0, 0(t0)
  mv s9, t0
  mv t0, s4
  mv t1, s9
  add t0, t0, t1
  mv s1, t0
  mv a0, s1
  addi sp, sp, 0
  li a7, 93
  ecall