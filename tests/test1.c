int f(int i){
    return i;
}
void m(){
    return;
}
int main(){
    int a = 1;
    m();
    return f(f(a)+a)+a;
}