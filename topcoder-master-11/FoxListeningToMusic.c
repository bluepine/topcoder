#include<stdio.h>
#include<stdlib.h>
#include<string.h>
void read_input(int input[50], int * L, int * T){
#define input_buf_len 16
  char input_buf[input_buf_len];
  *L = 0;
  int i, end = 0;
  while(!end){
    scanf("%s\n", input_buf);
    i = (int)strlen(input_buf);
    if('}' == input_buf[i-1]){
      end = 1;
      i--;
    }
    input_buf[i] = 0;
    if(*L)
      input[*L] = atoi(input_buf);
    else
      input[*L] = atoi(input_buf+1);
    (*L)++;
  }
  scanf("%d", T);
}

void dump_input(int input[50], int L, int T){
  int i;
  for(i=0; i<L; i++){
    printf("%d ", input[i]);
  }
  printf("\n");
  printf("%d\n", T);
}

int input[50];
int L;
double prob(int T, int start, int end){
  printf("%d, %d, %d\n", T, start, end);
  int i;
  double r = 0;
  if(start >= 0){
    if(T < input[start])
      if(start == end){
	return 1;
      }else{
	return 0;
      }
    T -= input[start];
  }
  for(i=0; i<L; i++){
    r += prob(T, i, end)/L;
  }
  return r;
}

int main(int argc, const char * argv[]){
  int T, i;
  double output[50];
  read_input(input, &L, &T);
  dump_input(input, L, T);
  printf("{");
  for(i=0; i<L; i++){
    double r = prob(T, -1, i);
    printf("%f", r);
    if(i==(L-1)){
      printf(" }\n");      
    }else{
      printf(", ");
    }
  }
  return 0;
}
