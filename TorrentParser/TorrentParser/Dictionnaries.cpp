#include "Dictionnaries.h"
#include "Parser.h"
#include "Global.h"


Dictionnaries::Dictionnaries(Parser& p_parser)
{
	this->parser = &p_parser;
	this->wordOrDef = 0;
	parseData();
	//cout << "Fin Dico" << endl;
}


Dictionnaries::~Dictionnaries()
{
}


void Dictionnaries::parseData()
{ // Parse the datas
	//cout << "Parsedata from dico" << endl;
	for (this->parser->offset = this->parser->offset; this->parser->offset < this->parser->fileSize; this->parser->offset++)
	{
		//cout << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		if (this->parser->datas[this->parser->offset] == 'e') break;
		nextDataType(this->parser->datas[this->parser->offset]);
		//cout << ", " << this->parser->datas[this->parser->offset] << endl;
	}
}



void Dictionnaries::nextDataType(char type)
{ // Determines the type of the next encountred data
	if (type == 'd')
	{ // Next data = new dictionnary
		//cout << "new dico from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		this->parser->offset++;
		if (this->wordOrDef == 0)
		{ // It's a word
			//cout << "new word from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
			this->word.push_back(new Global(new Dictionnaries(*this->parser)));
			this->wordOrDef = 1;
		}
		else
		{ // It's a definition
			//cout << "new def from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
			this->def.push_back(new Global(new Dictionnaries(*this->parser)));
			this->wordOrDef = 0;
		}
	}

	else if (type == 'l')
	{ // Next datas = new list
		this->parser->offset++;
		//cout << "new list from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		if (this->wordOrDef == 0)
		{ // It's a word
			//cout << "new word from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
			this->word.push_back(new Global(new Listes(*this->parser)));
			this->wordOrDef = 1;
		}
		else
		{ // It's a definition
			//cout << "new def from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
			this->def.push_back(new Global(new Listes(*this->parser)));
			this->wordOrDef = 0;
		}
	}

	else if (type <= '\x39' && type >= '\x30')
	{ // If it's a number we look for delimiters (":") to get the length of the next string
		//cout << "get length string from dico: " << type << endl;
		string s_lengthOfNextString;
		s_lengthOfNextString += type;

		this->parser->offset++;
		for (this->parser->offset = this->parser->offset; this->parser->offset < this->parser->fileSize; this->parser->offset++)
		{
			if (this->parser->datas[this->parser->offset] == ':')
			{ // Delimiters found
				this->parser->offset++;
				break;
			}
			else s_lengthOfNextString += this->parser->datas[this->parser->offset]; // Adds the current number to the end of the string
		}

		//cout << s_lengthOfNextString << endl;
		int i_lengthOfNextString = stoi(s_lengthOfNextString); // Converts the string to int
		getString(i_lengthOfNextString);
	}

	else if (type == '-' || type == 'i')
	{ // It's an integer (which constitutes a definition)
		//cout << "get an integer from dico: " << type << endl;
		string s_integer;
		this->parser->offset++;

		if (type == '-')
		{
			this->parser->offset++; // After the "-" there's always an "i" which is the begin tag of an integer
			s_integer += type;
		}

		for (this->parser->offset = this->parser->offset; this->parser->offset < this->parser->fileSize; this->parser->offset++)
		{
			if (this->parser->datas[this->parser->offset] == 'e')
			{ // Delimiters found
				break;
			}
			else s_integer += this->parser->datas[this->parser->offset]; // Adds the current number to the end of the string
		}

		//cout << s_integer << endl;

		if (this->wordOrDef == 0)
		{ // It's a word
		//	cout << "new word from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
			this->word.push_back(new Global(s_integer));
			this->wordOrDef = 1;
		}
		else
		{ // It's a definition
		//	cout << "new def from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
			this->def.push_back(new Global(s_integer));
			this->wordOrDef = 0;
		}
	}
}



void Dictionnaries::getString(int lengthOfString)
{ // Gets the string corresponding to the length

	string info;
	for (int i = 0; i < lengthOfString; i++)
	{
		info  += this->parser->datas[this->parser->offset];
		this->parser->offset++;
	}
	//cout << "string: " << info << ", length: " << lengthOfString << endl;

	this->parser->offset--; // Sets the cursor to the previous character for a proper analysis (to not interfer with the offset++ of the parseData function))

	if (this->wordOrDef == 0)
	{ // It's a word
	//	cout << "new word from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		this->word.push_back(new Global(info));
		this->wordOrDef = 1;
	}
	else
	{ // It's a definition
	//	cout << "new def from dico: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		this->def.push_back(new Global(info));
		this->wordOrDef = 0;
	}
}

void Dictionnaries::printDico()
{
	cout << "[ ";
	for (unsigned int i = 0; i < word.size(); i++)
	{
		word.at(i)->printGlobal();
		cout << " => ";
		def.at(i)->printGlobal();
		cout << endl;
	}
	cout << " ]" << endl;
}