; ModuleID = 'module'
source_filename = "module"

@x = global i32 1
@y = global i32 2
@z = global i32 3
@a = global i32 4
@b = global i32 5
@c = global i32 6
@d = global i32 7
@e = global i32 8
@f = global i32 9
@g = global i32 10
@h = global i32 11
@i = global i32 12
@j = global i32 13
@k = global i32 14
@l = global i32 15
@m = global i32 16
@n = global i32 17
@o = global i32 18
@p = global i32 19
@q = global i32 20

define i32 @main() {
mainEntry:
  %x1 = alloca i32, align 4
  store i32 1, i32* %x1, align 4
  %x2 = alloca i32, align 4
  store i32 2, i32* %x2, align 4
  %x3 = alloca i32, align 4
  store i32 3, i32* %x3, align 4
  %x4 = alloca i32, align 4
  store i32 4, i32* %x4, align 4
  %x5 = alloca i32, align 4
  store i32 5, i32* %x5, align 4
  %x6 = alloca i32, align 4
  store i32 6, i32* %x6, align 4
  %x7 = alloca i32, align 4
  store i32 7, i32* %x7, align 4
  %x8 = alloca i32, align 4
  store i32 8, i32* %x8, align 4
  %x9 = alloca i32, align 4
  store i32 9, i32* %x9, align 4
  %x10 = alloca i32, align 4
  store i32 10, i32* %x10, align 4
  %x11 = alloca i32, align 4
  store i32 11, i32* %x11, align 4
  %x12 = alloca i32, align 4
  store i32 12, i32* %x12, align 4
  %x13 = alloca i32, align 4
  store i32 13, i32* %x13, align 4
  %x14 = alloca i32, align 4
  store i32 14, i32* %x14, align 4
  %x15 = alloca i32, align 4
  store i32 15, i32* %x15, align 4
  %x16 = alloca i32, align 4
  store i32 16, i32* %x16, align 4
  %x17 = alloca i32, align 4
  store i32 17, i32* %x17, align 4
  %x18 = alloca i32, align 4
  store i32 18, i32* %x18, align 4
  %x19 = alloca i32, align 4
  store i32 19, i32* %x19, align 4
  %x20 = alloca i32, align 4
  store i32 20, i32* %x20, align 4
  %x = load i32, i32* @x, align 4
  %add = add i32 %x, 1
  store i32 %add, i32* @x, align 4
  %y = load i32, i32* @y, align 4
  %add1 = add i32 %y, 2
  store i32 %add1, i32* @y, align 4
  %z = load i32, i32* @z, align 4
  %add2 = add i32 %z, 3
  store i32 %add2, i32* @z, align 4
  %a = load i32, i32* @a, align 4
  %add3 = add i32 %a, 4
  store i32 %add3, i32* @a, align 4
  %b = load i32, i32* @b, align 4
  %add4 = add i32 %b, 5
  store i32 %add4, i32* @b, align 4
  %c = load i32, i32* @c, align 4
  %add5 = add i32 %c, 6
  store i32 %add5, i32* @c, align 4
  %d = load i32, i32* @d, align 4
  %add6 = add i32 %d, 7
  store i32 %add6, i32* @d, align 4
  %e = load i32, i32* @e, align 4
  %add7 = add i32 %e, 8
  store i32 %add7, i32* @e, align 4
  %f = load i32, i32* @f, align 4
  %add8 = add i32 %f, 9
  store i32 %add8, i32* @f, align 4
  %g = load i32, i32* @g, align 4
  %add9 = add i32 %g, 10
  store i32 %add9, i32* @g, align 4
  %h = load i32, i32* @h, align 4
  %add10 = add i32 %h, 11
  store i32 %add10, i32* @h, align 4
  %i = load i32, i32* @i, align 4
  %add11 = add i32 %i, 12
  store i32 %add11, i32* @i, align 4
  %j = load i32, i32* @j, align 4
  %add12 = add i32 %j, 13
  store i32 %add12, i32* @j, align 4
  %k = load i32, i32* @k, align 4
  %add13 = add i32 %k, 14
  store i32 %add13, i32* @k, align 4
  %l = load i32, i32* @l, align 4
  %add14 = add i32 %l, 15
  store i32 %add14, i32* @l, align 4
  %m = load i32, i32* @m, align 4
  %add15 = add i32 %m, 16
  store i32 %add15, i32* @m, align 4
  %n = load i32, i32* @n, align 4
  %add16 = add i32 %n, 17
  store i32 %add16, i32* @n, align 4
  %o = load i32, i32* @o, align 4
  %add17 = add i32 %o, 18
  store i32 %add17, i32* @o, align 4
  %p = load i32, i32* @p, align 4
  %add18 = add i32 %p, 19
  store i32 %add18, i32* @p, align 4
  %q = load i32, i32* @q, align 4
  %add19 = add i32 %q, 20
  store i32 %add19, i32* @q, align 4
  %x120 = load i32, i32* %x1, align 4
  %mul = mul i32 %x120, 2
  store i32 %mul, i32* %x1, align 4
  %x221 = load i32, i32* %x2, align 4
  %mul22 = mul i32 %x221, 2
  store i32 %mul22, i32* %x2, align 4
  %x323 = load i32, i32* %x3, align 4
  %mul24 = mul i32 %x323, 2
  store i32 %mul24, i32* %x3, align 4
  %x425 = load i32, i32* %x4, align 4
  %mul26 = mul i32 %x425, 2
  store i32 %mul26, i32* %x4, align 4
  %x527 = load i32, i32* %x5, align 4
  %mul28 = mul i32 %x527, 2
  store i32 %mul28, i32* %x5, align 4
  %x629 = load i32, i32* %x6, align 4
  %mul30 = mul i32 %x629, 2
  store i32 %mul30, i32* %x6, align 4
  %x731 = load i32, i32* %x7, align 4
  %mul32 = mul i32 %x731, 2
  store i32 %mul32, i32* %x7, align 4
  %x833 = load i32, i32* %x8, align 4
  %mul34 = mul i32 %x833, 2
  store i32 %mul34, i32* %x8, align 4
  %x935 = load i32, i32* %x9, align 4
  %mul36 = mul i32 %x935, 2
  store i32 %mul36, i32* %x9, align 4
  %x1037 = load i32, i32* %x10, align 4
  %mul38 = mul i32 %x1037, 2
  store i32 %mul38, i32* %x10, align 4
  %x1139 = load i32, i32* %x11, align 4
  %mul40 = mul i32 %x1139, 2
  store i32 %mul40, i32* %x11, align 4
  %x1241 = load i32, i32* %x12, align 4
  %mul42 = mul i32 %x1241, 2
  store i32 %mul42, i32* %x12, align 4
  %x1343 = load i32, i32* %x13, align 4
  %mul44 = mul i32 %x1343, 2
  store i32 %mul44, i32* %x13, align 4
  %x1445 = load i32, i32* %x14, align 4
  %mul46 = mul i32 %x1445, 2
  store i32 %mul46, i32* %x14, align 4
  %x1547 = load i32, i32* %x15, align 4
  %mul48 = mul i32 %x1547, 2
  store i32 %mul48, i32* %x15, align 4
  %x1649 = load i32, i32* %x16, align 4
  %mul50 = mul i32 %x1649, 2
  store i32 %mul50, i32* %x16, align 4
  %x1751 = load i32, i32* %x17, align 4
  %mul52 = mul i32 %x1751, 2
  store i32 %mul52, i32* %x17, align 4
  %x1853 = load i32, i32* %x18, align 4
  %mul54 = mul i32 %x1853, 2
  store i32 %mul54, i32* %x18, align 4
  %x1955 = load i32, i32* %x19, align 4
  %mul56 = mul i32 %x1955, 2
  store i32 %mul56, i32* %x19, align 4
  %x2057 = load i32, i32* %x20, align 4
  %mul58 = mul i32 %x2057, 2
  store i32 %mul58, i32* %x20, align 4
  %x159 = load i32, i32* %x1, align 4
  %x260 = load i32, i32* %x2, align 4
  %add61 = add i32 %x159, %x260
  %x362 = load i32, i32* %x3, align 4
  %add63 = add i32 %add61, %x362
  %x464 = load i32, i32* %x4, align 4
  %add65 = add i32 %add63, %x464
  %x566 = load i32, i32* %x5, align 4
  %add67 = add i32 %add65, %x566
  %x668 = load i32, i32* %x6, align 4
  %add69 = add i32 %add67, %x668
  %x770 = load i32, i32* %x7, align 4
  %add71 = add i32 %add69, %x770
  %x872 = load i32, i32* %x8, align 4
  %add73 = add i32 %add71, %x872
  %x974 = load i32, i32* %x9, align 4
  %add75 = add i32 %add73, %x974
  %x1076 = load i32, i32* %x10, align 4
  %add77 = add i32 %add75, %x1076
  %x1178 = load i32, i32* %x11, align 4
  %add79 = add i32 %add77, %x1178
  %x1280 = load i32, i32* %x12, align 4
  %add81 = add i32 %add79, %x1280
  %x1382 = load i32, i32* %x13, align 4
  %add83 = add i32 %add81, %x1382
  %x1484 = load i32, i32* %x14, align 4
  %add85 = add i32 %add83, %x1484
  %x1586 = load i32, i32* %x15, align 4
  %add87 = add i32 %add85, %x1586
  %x1688 = load i32, i32* %x16, align 4
  %add89 = add i32 %add87, %x1688
  %x1790 = load i32, i32* %x17, align 4
  %add91 = add i32 %add89, %x1790
  %x1892 = load i32, i32* %x18, align 4
  %add93 = add i32 %add91, %x1892
  %x1994 = load i32, i32* %x19, align 4
  %add95 = add i32 %add93, %x1994
  %x2096 = load i32, i32* %x20, align 4
  %add97 = add i32 %add95, %x2096
  %x98 = load i32, i32* @x, align 4
  %add99 = add i32 %add97, %x98
  %y100 = load i32, i32* @y, align 4
  %add101 = add i32 %add99, %y100
  %z102 = load i32, i32* @z, align 4
  %add103 = add i32 %add101, %z102
  %a104 = load i32, i32* @a, align 4
  %add105 = add i32 %add103, %a104
  %b106 = load i32, i32* @b, align 4
  %add107 = add i32 %add105, %b106
  %c108 = load i32, i32* @c, align 4
  %add109 = add i32 %add107, %c108
  %d110 = load i32, i32* @d, align 4
  %add111 = add i32 %add109, %d110
  %e112 = load i32, i32* @e, align 4
  %add113 = add i32 %add111, %e112
  %f114 = load i32, i32* @f, align 4
  %add115 = add i32 %add113, %f114
  %g116 = load i32, i32* @g, align 4
  %add117 = add i32 %add115, %g116
  %h118 = load i32, i32* @h, align 4
  %add119 = add i32 %add117, %h118
  %i120 = load i32, i32* @i, align 4
  %add121 = add i32 %add119, %i120
  %j122 = load i32, i32* @j, align 4
  %add123 = add i32 %add121, %j122
  %k124 = load i32, i32* @k, align 4
  %add125 = add i32 %add123, %k124
  %l126 = load i32, i32* @l, align 4
  %add127 = add i32 %add125, %l126
  %m128 = load i32, i32* @m, align 4
  %add129 = add i32 %add127, %m128
  %n130 = load i32, i32* @n, align 4
  %add131 = add i32 %add129, %n130
  %o132 = load i32, i32* @o, align 4
  %add133 = add i32 %add131, %o132
  %p134 = load i32, i32* @p, align 4
  %add135 = add i32 %add133, %p134
  %q136 = load i32, i32* @q, align 4
  %add137 = add i32 %add135, %q136
  ret i32 %add137
}
