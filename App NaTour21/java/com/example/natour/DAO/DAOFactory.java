package com.example.natour.DAO;

public class DAOFactory {
    private static DAOFactory DaoFactory;

    public static DAOFactory getDaoFactory() {
        if (DaoFactory == null) {
            DaoFactory = new DAOFactory();
        }
        return DaoFactory;
    }

    public UtenteDAO getUtenteDAO(){
        return new UtenteDAORetrofit();
    }

    public ItinerarioDAO getItinerarioDAO(){

        return new ItinerarioDAORetrofit();
    }

    public PointDAO getPointDAO(){
        return new PointDAORetrofit();
    }

    public CompilationDAO getCompilationDAO(){

        return new CompilationDAORetrofit();
    }

}
