#pragma once
#ifndef GLOBAL_H
#define GLOBAL_H

#include <string>
#include <iostream>

using namespace std;

class Listes;
class Dictionnaries;

class Global
{
public:
	Listes* aList;
	Dictionnaries* aDico;
	string aString;

public:
	Global(Listes* p_list, Dictionnaries* p_dico, string p_string);
	Global(Dictionnaries* p_dico);
	Global(Listes* p_list);
	Global(string p_string);
	~Global();
	void printGlobal();
};





#endif // !GLOBAL_H


