package ankhmorpork.datainput;

import ankhmorpork.util.Environment;

/**
 * Ankh-Morpork Keyboard Scanner Adapter
 * This act as the worst AI possible for people with no friend :'(
 * @author Pascal
 * @since Build 2
 */
public class AMScannerAI extends AMScanner
{	
	/**
	 * @return Worst AI ever ;p
	 */
	@Override
	public String next()
	{
		int value = Environment.randInt(1, 8);
		if(value < 4)
		{
			value = 1;
		}
		else
		{
			value = Environment.randInt(1, 8);
		}
		return value + "";
	}	
}
