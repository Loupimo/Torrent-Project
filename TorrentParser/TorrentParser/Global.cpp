#include "Global.h"
#include "Dictionnaries.h"
#include "Listes.h"


Global::Global(Listes* p_list, Dictionnaries* p_dico, string p_string)
{
	this->aList = p_list;
	this->aDico = p_dico;
	this->aString = p_string;
}


Global::Global(Dictionnaries* p_dico)
{
	this->aList = NULL;
	this->aDico = p_dico;
	this->aString = "";
}


Global::Global(Listes* p_list)
{
	this->aList = p_list;
	this->aDico = NULL;
	this->aString = "";
}


Global::Global(string p_string)
{
	this->aList = NULL;
	this->aDico = NULL;
	this->aString = p_string;
}

Global::~Global()
{
}

void Global::printGlobal()
{
	if (aDico != NULL) aDico->printDico();
	if (aList != NULL) aList->printListes();
	if (aString != "") cout << aString;
}
