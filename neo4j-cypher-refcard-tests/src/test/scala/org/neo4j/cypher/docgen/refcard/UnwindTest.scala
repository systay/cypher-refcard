/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.docgen.refcard

import org.neo4j.cypher.{ ExecutionResult, QueryStatisticsTestSupport }
import org.neo4j.cypher.docgen.RefcardTest

class UnwindTest extends RefcardTest with QueryStatisticsTestSupport {
  val graphDescription = List("ROOT ACTED_IN A:Person", "A ACTED_IN B:Person", "B ACTED_IN C:Person", "C ACTED_IN ROOT")
  val title = "UNWIND"
  val css = "read c2-2 c3-2 c4-2 c5-2"

  override def assert(name: String, result: ExecutionResult) {
    name match {
      case "related" =>
        assertStats(result, nodesCreated = 0)
        assert(result.toList.size === 3)
    }
  }

  override def parameters(name: String): Map[String, Any] =
    name match {
      case "" =>
        Map()
    }

  override val properties: Map[String, Map[String, Any]] = Map(
    "A" -> Map("name" -> "Lucy Liu"),
    "B" -> Map("name" -> "Someone"),
    "C" -> Map("name" -> "Kevin Bacon"))

  def text = """
###assertion=related
//

MATCH p = shortestPath( (lucy:Person {name:"Lucy Liu"})-[:ACTED_IN*]-(bacon:Person {name:"Kevin Bacon"}) )
UNWIND nodes(p) as n

RETURN n.name###

Return a set of actors that form the shortest acquaintance links between Lucy Liu and Kevin Bacon."""
}
