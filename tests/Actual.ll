; ModuleID = 'module'
source_filename = "module"

define i32 @b(i32 %0) {
bEntry:
  %pointer = alloca i32, align 4
  store i32 %0, i32* %pointer, align 4
  store i32 0, i32* %pointer, align 4
  %pointer1 = load i32, i32* %pointer, align 4
  ret i32 %pointer1
}

define i32 @c(i32 %0) {
cEntry:
  %pointer = alloca i32, align 4
  store i32 %0, i32* %pointer, align 4
  %pointer1 = load i32, i32* %pointer, align 4
  ret i32 %pointer1
}
