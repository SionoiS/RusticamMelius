package sionois.rusticammelius;

public class ModRef 
{
        public static final String ModID = "rusticammelius";
        public static final String ModName = "RusticamMelius";

        public static final int VersionMajor = 1;
        public static final int VersionMinor = 0;
        public static final int VersionRevision = 0;

        public static final String ModVersion = VersionMajor + "." + VersionMinor + "." + VersionRevision;
        
        public static final String ModDependencies = "required-after:terrafirmacraft";
        
        public static final String SERVER_PROXY_CLASS = "sionois.rusticammelius.CommonProxy";
        public static final String CLIENT_PROXY_CLASS = "sionois.rusticammelius.ClientProxy";
}
