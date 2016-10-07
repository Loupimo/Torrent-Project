#include "Listes.h"
#include "Parser.h"
#include "Global.h"
#include "Dictionnaries.h"


Listes::Listes(Parser& p_parser)
{
	this->parser = &p_parser;
	parseData();
	//cout << "Fin liste" << endl;
}


Listes::~Listes()
{
}




void Listes::parseData()
{ // Parse the datas
	//cout << "Parsedata from listes" << endl;
	for (this->parser->offset = this->parser->offset; this->parser->offset < this->parser->fileSize; this->parser->offset++)
	{
		//cout << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		if (this->parser->datas[this->parser->offset] == 'e') break;
		nextDataType(this->parser->datas[this->parser->offset]);
		//cout << ", " << this->parser->datas[this->parser->offset] << endl;
	}
}



void Listes::nextDataType(char type)
{ // Determines the type of the next encountred data
	if (type == 'd')
	{ // Next data = new dictionnary
		//cout << "new dico from listes: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		this->parser->offset++;
		this->list.push_back(new Global(new Dictionnaries(*this->parser)));
	}

	else if (type == 'l')
	{ // Next datas = new list
		this->parser->offset++;
		//cout << "new list from listes: " << this->parser->offset << ", " << this->parser->datas[this->parser->offset] << endl;
		this->list.push_back(new Global(new Listes(*this->parser)));
	}

	else if (type <= '\x39' && type >= '\x30')
	{ // If it's a number we look for delimiters (":") to get the length of the next string
		//cout << "get length string from listes: " << type << endl;
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
		//cout << "get an integer from listes: " << type << endl;
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
		this->list.push_back(new Global(s_integer));
	}
}



void Listes::getString(int lengthOfString)
{ // Gets the string corresponding to the length

	string info;
	for (int i = 0; i < lengthOfString; i++)
	{
		info += this->parser->datas[this->parser->offset];
		this->parser->offset++;
	}
	//cout << "string: " << info << ", length: " << lengthOfString << endl;

	this->parser->offset--; // Sets the cursor to the previous character for a proper analysis (to not interfer with the offset++ of the parseData function))
	this->list.push_back(new Global(info));
}

void Listes::printListes()
{
	cout << "{ ";
	for (unsigned int i = 0; i < list.size(); i++)
	{	
		list.at(i)->printGlobal();
		if (i < list.size()-1) cout << ", ";
	}
	cout << " }" << endl;
}