#pragma once
#ifndef DICTIONNARIES_H
#define DICTIONNARIES_H

#include <string>
#include <vector>

using namespace std;

class Parser;
class Listes;
class Global;

class Dictionnaries
{
public:
	Parser* parser;				// The common parser where are stored all the datas, file size and the offset
	vector <Global *> word;		// A word of dictionnary. It could be a List, a string and even a dictionnary
	vector <Global *> def;		// The definition of corresponding word. It could be a List, a string and even a dictionnary
	int wordOrDef;				// A flag used to know if the current string is a word or a definition (0 = word, 1 = definition)

public:
	Dictionnaries(Parser& p_parser);
	~Dictionnaries();
	void parseData();
	void nextDataType(char type);
	void getString(int lengthOfString);
	void printDico();
};



#endif // !DICTIONNARIES_H


