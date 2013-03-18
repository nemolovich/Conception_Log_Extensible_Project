package view.table;

import java.util.Comparator;

import javax.swing.JCheckBox;

public class JCheckBoxComparator implements Comparator<JCheckBox>
{

	@Override
	public int compare(JCheckBox chk1, JCheckBox chk2)
	{
		boolean is1=chk1.isSelected();
		boolean is2=chk2.isSelected();
		return (is1==is2)?0:(is1)?-1:1;
	}
}