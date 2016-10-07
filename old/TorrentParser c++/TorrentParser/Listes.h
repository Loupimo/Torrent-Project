#pragma once
#ifndef LISTES_H
#define LISTES_H

#include <vector>
#include <iostream>

using namespace std;

class Parser;
class Global;

class Listes
{
public:
	Parser* parser;
	vector <Global*> list;

public:
	Listes(Parser& p_parser);
	~Listes();
	void parseData();
	void nextDataType(char type);
	void getString(int lengthOfString);
	void printListes();

};


#endif // !LISTES_H


