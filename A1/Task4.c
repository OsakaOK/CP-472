#include <stdio.h>
#include <stdlib.h>

#define MAX_STUDENTS 50

// WITHOUT STRUCT
int student_ids[MAX_STUDENTS];
char student_names[MAX_STUDENTS][50];
int student_ages[MAX_STUDENTS];
int num_students = 0;

void addStudent()
{
    if (num_students < MAX_STUDENTS)
    {
        printf("Enter student ID: ");
        scanf("%d", &student_ids[num_students]);
        printf("Enter student name: ");
        scanf("%s", student_names[num_students]);
        printf("Enter student age: ");
        scanf("%d", &student_ages[num_students]);
        num_students++;
        printf("Student added successfully!\n");
    }
    else
    {
        printf("Database full. Cannot add more students.\n");
    }
}

void getID(int id)
{
    int found = 0;
    for (int i = 0; i < num_students; i++)
    {
        if (student_ids[i] == id)
        {
            printf("Student ID: %d\nName: %s\nAge: %d\n", student_ids[i], student_names[i], student_ages[i]);
            found = 1;
            break;
        }
    }
    if (!found)
    {
        printf("Student with ID %d not found.\n", id);
    }
}

void displayStudents()
{
    for (int i = 0; i < num_students; i++)
    {
        printf("Student ID: %d\nName: %s\nAge: %d\n\n", student_ids[i], student_names[i], student_ages[i]);
    }
}

// USING STRUCT
struct Student
{
    int id;
    char name[50];
    int age;
};

struct Student students[MAX_STUDENTS];

void addStudent_Struct()
{
    if (num_students < MAX_STUDENTS)
    {
        printf("Enter student ID: ");
        scanf("%d", &students[num_students].id);
        printf("Enter student name: ");
        scanf("%s", students[num_students].name);
        printf("Enter student age: ");
        scanf("%d", &students[num_students].age);
        num_students++;
        printf("Student added successfully!\n");
    }
    else
    {
        printf("Database full. Cannot add more students.\n");
    }
}

void getID_Struct(int id)
{
    int found = 0;
    for (int i = 0; i < num_students; i++)
    {
        if (students[i].id == id)
        {
            printf("Student ID: %d\nName: %s\nAge: %d\n", students[i].id, students[i].name, students[i].age);
            found = 1;
            break;
        }
    }
    if (!found)
    {
        printf("Student with ID %d not found.\n", id);
    }
}

void displayStudents_Struct()
{
    for (int i = 0; i < num_students; i++)
    {
        printf("Student ID: %d\nName: %s\nAge: %d\n\n", students[i].id, students[i].name, students[i].age);
    }
}

int main()
{
    int choice, id;

    do
    {
        printf("\n1. Add new student \n2. Retrieve student information by ID \n3. Display all students \n");
        printf("4. Add new student (With Structs)\n5. Retrieve student information by ID (With Structs)\n6. Display all students (With Structs)\n");
        printf("7. Quit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice)
        {
        case 1:
            addStudent();
            break;
        case 2:
            printf("Enter student ID: ");
            scanf("%d", &id);
            getID(id);
            break;
        case 3:
            displayStudents();
            break;
        case 4:
            addStudent_Struct();
            break;
        case 5:
            printf("Enter student ID: ");
            scanf("%d", &id);
            getID_Struct(id);
            break;
        case 6:
            displayStudents_Struct();
            break;
        case 7:
            printf("Exiting program.\n");
            break;
        default:
            printf("Invalid choice. Please enter a valid option.\n");
        }
    } while (choice != 7);

    return 0;
}
