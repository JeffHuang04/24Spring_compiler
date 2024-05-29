; ModuleID = 'module'
source_filename = "module"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 0, i32* %a, align 4
  %count = alloca i32, align 4
  store i32 0, i32* %count, align 4
  %a1 = load i32, i32* %a, align 4
  %GT = icmp sgt i32 %a1, 0
  %GT2 = zext i1 %GT to i32
  %count3 = load i32, i32* %count, align 4
  %GT4 = icmp sgt i32 %count3, 0
  %GT5 = zext i1 %GT4 to i32
  %cond = icmp ne i32 %GT2, 0
  br i1 %cond, label %result, label %right

left:                                             ; No predecessors!

right:                                            ; preds = %mainEntry
  %cond6 = icmp ne i32 %GT5, 0
  br i1 %cond6, label %result, label %result

result:                                           ; preds = %right, %right, %mainEntry
  %orPhi = phi i1 [ %right, <null operand!> ], [ <null operand!>, <null operand!> ]
  %OR = zext i1 %orPhi to i32
  %cond7 = icmp ne i32 %OR, 0
  br i1 %cond7, label %if_true, label %if_false

if_true:                                          ; preds = %result
  store i32 1, i32* %a, align 4
  br label %next

if_false:                                         ; preds = %result
  br label %next

next:                                             ; preds = %if_false, %if_true
  %count8 = load i32, i32* %count, align 4
  ret i32 %count8
}
