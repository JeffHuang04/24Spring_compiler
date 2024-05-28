; ModuleID = 'module'
source_filename = "module"

define void @a() {
aEntry:
  ret void
}

define i32 @main() {
mainEntry:
  %c = alloca i32, align 4
  store i32 0, i32* %c, align 4
  %count = alloca i32, align 4
  store i32 0, i32* %count, align 4
  ret i32 0
}
