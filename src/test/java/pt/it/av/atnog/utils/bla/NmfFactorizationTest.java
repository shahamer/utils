package pt.it.av.atnog.utils.bla;

import org.junit.Test;
import pt.it.av.atnog.utils.ArrayUtils;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link NmfFactorization}.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class NmfFactorizationTest {

  @Test
  public void test_identity_nmf() {
    Matrix V = Matrix.identity(5);
    Matrix WH[] = NmfFactorization.nmf_mu2(V, 5, 1000, 0.01);
    Matrix wh = WH[0].mul(WH[1]);
    double cost = ArrayUtils.euclideanDistance(V.data, 0, wh.data, 0, V.data.length);
    assertTrue(cost <= 1.0);
  }
}
