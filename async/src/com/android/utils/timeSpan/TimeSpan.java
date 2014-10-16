package com.android.utils.timeSpan;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: vicboma
 * Date: 07/10/13
 * Time: 12:16
 * Clase que permite hacer una cuenta atras mediante un intervalo y permite ejecutar una callback
 */
public class TimeSpan implements ITimeSpan
{
	public static ITimeSpan create(String name, long milliseconds, long interval, ICallBackTimeSpan callBack)
	{
		return new TimeSpan(name, milliseconds ,interval, callBack);
	}

	/** variable que indica el intervalo de tiempo de la cuenta atras */
	private long _interval;
	/** variable que lleva la cuenta total en milli sengundos */
	private long _totalSecondsMilliseconds;
	/** Timer Task de la clase */
	private TimerTask _timerTask;
	/** Timer de la clase */
	private Timer _timer;
	/** variable que indica el tiempo actual de la cuenta atras */
	private long _actualTimeMillis;
	/** variable que indica si ha finalizado el timer */
	private boolean _isFinish;
	/** Interfaz que permite ejecutar una callback */
	private ICallBackTimeSpan _callBack;
    private boolean pSleeping;

    /**
	 * Constructor de la clase
	 * @param milliseconds
	 * @param interval
	 * @param callBack
	 */
	TimeSpan(String name, long milliseconds, long interval, ICallBackTimeSpan callBack )
	{
		this._totalSecondsMilliseconds = milliseconds;
		this._interval = interval;
		this._timer = new Timer(name);
		this._actualTimeMillis = 0;
		this._isFinish = false;
        this.pSleeping = false;
		this._callBack = callBack;
	}
	/**
	 * Metodo que para el timeSpan
	 */
	public ITimeSpan stop()
	{
		_timerTask.cancel();
		_timer.cancel();
		_timer.purge();
		_actualTimeMillis = 0;
		_isFinish = true;
		return this;
	}

    public Boolean isSleeping() {
        return pSleeping;
    }

    public void sleepThread(Integer milisecs) {
        pSleeping = true;
        try {
            Thread.sleep(milisecs);
        } catch (Exception e) {
            System.err.println("Exception sleepThread");
        }
        pSleeping = false;
    }

    /**
	 * Metodo que arranca el TimeSpan
	 */
	public ITimeSpan runOne()
	{
		_timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				_actualTimeMillis -= _interval;

				if ((_actualTimeMillis) <= 0 )
				{
					if(_callBack!=null)
						_callBack.run();
					stop();
				}
			}
		};

		_actualTimeMillis = _totalSecondsMilliseconds;
		_timer.schedule(_timerTask, 0 , _interval);

		return this;
	}

	/**
	 * Metodo que arranca el TimeSpan
	 */
	public ITimeSpan run()
	{
		_timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				_actualTimeMillis -= _interval;

				if ((_actualTimeMillis) <= 0 )
				{
					if(_callBack!=null)
						_callBack.run();
				}
			}
		};

		_actualTimeMillis = _totalSecondsMilliseconds;
		_timer.schedule(_timerTask, 0 , _interval);

		return this;
	}



	/**
	 * Metodo consultor que permite saber si el timeSpan ha finalizado
	 * @return
	 */
	public boolean isFinish()
	{
		return this._isFinish;
	}

	/**
	 * Metodo que devuelve el tiempo de ejecucion actual
	 * @return
	 */
	public long getActualTimeMillis()
	{
		return this._actualTimeMillis;
	}

	/**
	 * Metodo que permite saber el tiempo total a contrarrestar
	 * @return
	 */
	public long getTotalMilliseconds()
	{
		return this._totalSecondsMilliseconds;
	}

	/**
	 * Metodo que permite comparar dos objectos
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj)
	{
		return compareTo((ITimeSpan)obj) == 0;
	}

	/**
	 * Metodo que pemrite comprar dos objectos de tipo timeSpan
	 * @param timeSpan
	 * @return
	 */
	public int compareTo(ITimeSpan timeSpan)
	{
		return (this._totalSecondsMilliseconds == timeSpan.getTotalMilliseconds()) ? 0 : -1;
	}

	/**
	 * Metodo ToString
	 * @return
	 */
	public String toString()
	{
		return "TimeSpan(" + _totalSecondsMilliseconds + " s)";
	}

}
