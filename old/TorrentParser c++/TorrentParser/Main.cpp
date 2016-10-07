#include "Parser.h"
#include <string>

using namespace std;

int main(int argc, char *argv[])
{
	if (argv[1] != NULL)
	{
		string torrentName = argv[1];
		Parser parse (torrentName);
	}
	else
	{
		string oo = "D:\\Documents\\Visual Studio 2015\\Projects\\TorrentParser\\Debug\\Assassins_Creed_IV_Black_Flag_Update_v1_02_with_DLC-RELOADED.torrent";
		Parser parse(oo);
	}
	system("PAUSE");
}