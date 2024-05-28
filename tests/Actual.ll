; ModuleID = 'module'
source_filename = "module"

define i32 @f(i32 %0, i32 %1) {
fEntry:
  %i = alloca i32, align 4
  %a = alloca i32, align 4
  store i32 %0, i32* %i, align 4
  store i32 %1, i32* %a, align 4
  ret i32 0
}

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 1, i32* %a, align 4
  %i = alloca i32, align 4
  store i32 5, i32* %i, align 4
  %a1 = load i32, i32* %a, align 4
  %i2 = load i32, i32* %i, align 4
  %f = call i32 @f(i32 %a1, i32 %i2)
  ret i32 %f
}
