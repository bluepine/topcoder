//song
#include<stdio.h>
#include<stdlib.h>
#include<string.h>

struct bfs_stack_node{
  struct bfs_stack_node * parent;//last in path
  struct bfs_stack_node * next;//next in stack
  int v;
};

typedef struct bfs_stack_node bfs_stack_node;
typedef struct bfs_stack_node * bfs_stack;

void error(const char * msg){
  printf("%s\n", msg);
  exit(-1);
}

void push(bfs_stack * stack, bfs_stack_node * parent, int v){
  bfs_stack_node * p = malloc(sizeof(bfs_stack_node));
  if(!p)
    error("malloc failed");
  p->parent = parent;
  p->next = *stack;
  *stack = p;
}

void pop(bfs_stack * stack){
  bfs_stack_node * p = *stack;
  *stack = p->next;
  free(p);
}

void read_input(int input[50], int * L){
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
}

void dump_input(int input[50], int L){
  int i;
  for(i=0; i<L; i++){
    printf("%d ", input[i]);
  }
  printf("\n");
}

int main(int argc, const char * argv[]){
  int input[50];
  int L;
  read_input(input, &L);
  dump_input(input, L);
}
