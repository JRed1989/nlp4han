package org.nlp4han.coref.hobbs;

/**
 * Mention属性类
 * 
 * @author 杨智超
 *
 */
public class MentionAttribute
{

	public MentionAttribute()
	{
		this.ani = Animacy.UNKNOWN;
		this.gen = Gender.UNKNOWN;
		this.num = Number.UNKNOWN;
		this.per = Person.UNKNOWN;
	}

	/**
	 * 性别： MALE——男性；FEMALE——女性；UNKNOWN——不确定；
	 */
	public enum Gender
	{
		MALE, FEMALE, UNKNOWN;
	}

	/**
	 * 数量： SINGULAR——单数；PLURAL——复数；UNKNOWN——不确定；
	 */
	public enum Number
	{
		SINGULAR, PLURAL, UNKNOWN;
	}

	/**
	 * 动物性： TRUE——动物；FALSE——非动物；UNKNOWN——不确定；
	 */
	public enum Animacy
	{
		TRUE, FALSE, UNKNOWN;
	}

	/**
	 * 人： TRUE——动物；FALSE——非动物；UNKNOWN——不确定；
	 */
	public enum Person
	{
		TRUE, FALSE, UNKNOWN;
	}

	private Gender gen;
	private Number num;
	private Animacy ani;
	private Person per;

	public Gender getGen()
	{
		return gen;
	}

	public void setGen(Gender gen)
	{
		this.gen = gen;
	}

	public Number getNum()
	{
		return num;
	}

	public void setNum(Number num)
	{
		this.num = num;
	}

	public Animacy getAni()
	{
		return ani;
	}

	public void setAni(Animacy ani)
	{
		this.ani = ani;
	}

	public Person getPer()
	{
		return per;
	}

	public void setPer(Person per)
	{
		this.per = per;
	}

}
