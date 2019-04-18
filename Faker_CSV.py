# Geoffrey Hughes
# 002306123
# ghughes@chapman.edu
# Prof. Rene German
# CPSC 408 - Database Management
# Spring 2019
# Assignment 3


# This python code creates a CSV file named whatever the user wants, and with as many tuples as the user wants
# The tuples are written as such: (first name, last name, title) for an author's first and last name and a book title
# These tuples are generated to be read and uploaded by a different java file into the Google Cloud in MySQL


from faker import Faker
import csv

faker = Faker()


def makeCSV():

    with open(fileName, 'a') as csvfile:

        fields = ['author_first', 'author_last', 'title']
        writer = csv.DictWriter(csvfile, fieldnames=fields)
        writer.writeheader()

        for i in range(int(numTuples)):

            writer.writerow(
                {
                    'author_first': faker.first_name(),
                    'author_last': faker.last_name(),
                    'title': faker.street_name()
                }
            )


if __name__ == '__main__':

    fileName = raw_input('Enter file name to create: ')
    fileName = fileName + '.csv'
    numTuples = raw_input('Enter number of tuples to create: ')
    makeCSV()
