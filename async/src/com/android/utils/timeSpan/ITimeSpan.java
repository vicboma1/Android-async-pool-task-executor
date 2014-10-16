package com.android.utils.timeSpan;


/**
 * Created with IntelliJ IDEA.
 * User: vicboma
 * Date: 07/10/13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
 public interface ITimeSpan
{

	/**
	 * Metodo que para el timeSpan
	 */
	 ITimeSpan stop();

	/**
	 * Metodo que lanza la cuenta atrás
	 */
	 ITimeSpan runOne();

	/**
	 * Metodo que lanza la cuenta atrás
	 */
	 ITimeSpan run();

	/**
	 * Metodo que indica si el timerSpan ha finalizado
	 * @return
	 */
	 boolean isFinish();

	/**
	 * Metodo que devuelve el tiempo actual en milliseconds
	 * @return
	 */
	 long getActualTimeMillis();

	/**
	 * Metodo que devuelve el timepo total en miliseconds
	 * @return
	 */
	 long getTotalMilliseconds();

	/**
	 * Metodo que compara dos objectos
	 * @param obj
	 * @return
	 */
	 boolean equals(Object obj);

	/**
	 * Metodo que compara 2 objetos timeSpan
	 * @param timeSpan
	 * @return
	 */
	 int compareTo(ITimeSpan timeSpan);

	/**
	 * Metodo to String de la clase
	 * @return
	 */
	 String toString();

     void sleepThread(Integer millisecond);

     Boolean isSleeping();
}
