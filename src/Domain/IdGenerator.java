package Domain;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Andreas Poulsen on 28-Apr-15.
 */
    public final class IdGenerator {
        private SecureRandom random = new SecureRandom();

        public String nextNonce() {
            return new BigInteger(130, random).toString(32);
        }
    }

