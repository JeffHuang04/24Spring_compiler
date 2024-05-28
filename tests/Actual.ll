; ModuleID = 'module'
source_filename = "module"

define i32 @f(i32 %0) {
fEntry:
  %i = alloca i32, align 4
  store i32 %0, i32* %i, align 4
  %i1 = load i32, i32* %i, align 4
  ret i32 %i1
}

define void @m() {
mEntry:
  ret void
}

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 1, i32* %a, align 4
  %m = call void @m()
  %a1 = load i32, i32* %a, align 4
  %f = call i32 @f(i32 %a1)
  %a2 = load i32, i32* %a, align 4
  %add = add i32 %f, %a2
  %f3 = call i32 @f(i32 %add)
  %a4 = load i32, i32* %a, align 4
  %add5 = add i32 %f3, %a4
  ret i32 %add5
}
