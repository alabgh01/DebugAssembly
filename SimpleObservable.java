import java.util.ArrayList;


public class SimpleObservable implements Observable
{

    private ArrayList< Observer > myObservers;

    public SimpleObservable()
    {

        myObservers = new ArrayList< Observer >();

        }

    @Override
    public void attach(Observer anotherObserver)
    {

        if (anotherObserver != null && ! myObservers.contains(anotherObserver))
            myObservers.add(anotherObserver);

        }

    @Override
    public void detach(Observer currentObserver)
    {

        myObservers.remove(currentObserver);

        }

    @Override
    public void announceChange()
    {

        for (Observer currentObserver : myObservers)
            currentObserver.update();

        }

    }
