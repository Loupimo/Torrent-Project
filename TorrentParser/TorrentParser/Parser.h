#pragma once
#ifndef PARSER_H
#define PARSER_H

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include "Listes.h"

using namespace std;

class Dictionnaries;
class Global;

class Parser
{
public:
	int fileSize;					// The file size
	char * datas;					// The datas that contains the file
	int offset;						// The cursor position in the file
	ifstream torrentFile;		    // The current torrent file
	vector <Dictionnaries> dicos;	// An array of all "dictionnaries" present in the file
	vector <Listes> listes;			// An array of all "lists" prensent in the file
	vector <string> chaines;		// An array of all the string present in the file
	vector <Global*> torrentParsed; // An array that contains every parsed info

public:
	Parser();
	Parser(string torrentFileName);
	Parser(Parser& parser);
	~Parser();
	void readFile();
	void parseData();
	void nextDataType(char type);
	void getString(int lengthOfString);
};



#endif // !PARSER_H



