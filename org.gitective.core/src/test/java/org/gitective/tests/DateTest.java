/*
 * Copyright (c) 2011 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.gitective.tests;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorDateFilter;
import org.gitective.core.filter.commit.CommitCountFilter;
import org.gitective.core.filter.commit.CommitterDateFilter;
import org.gitective.core.service.CommitFinder;

/**
 * 
 */
public class DateTest extends GitTestCase {

	/**
	 * Unit test of {@link AuthorDateFilter}
	 * 
	 * @throws Exception
	 */
	public void testAuthorMatch() throws Exception {
		add("file.txt", "a");
		Thread.sleep(1001);
		author = new PersonIdent("Test Author", "author@test.com");
		RevCommit second = add("file.txt", "b");
		CommitCountFilter count = new CommitCountFilter();

		CommitFinder finder = new CommitFinder(testRepo);
		finder.setFilter(new AndCommitFilter().add(new AuthorDateFilter(second
				.getAuthorIdent().getWhen()), count));
		finder.find();
		assertEquals(1, count.getCount());
	}

	/**
	 * Unit test of {@link AuthorDateFilter}
	 * 
	 * @throws Exception
	 */
	public void testAuthorNonMatch() throws Exception {
		RevCommit first = add("file.txt", "a");
		CommitCountFilter count = new CommitCountFilter();

		CommitFinder finder = new CommitFinder(testRepo);
		finder.setFilter(new AndCommitFilter().add(new AuthorDateFilter(first
				.getAuthorIdent().getWhen().getTime() + 1), count));
		finder.find();
		assertEquals(0, count.getCount());
	}

	/**
	 * Unit test of {@link CommitterDateFilter}
	 * 
	 * @throws Exception
	 */
	public void testCommitterMatch() throws Exception {
		add("file.txt", "a");
		Thread.sleep(1001);
		committer = new PersonIdent("Test committer", "commit@test.com");
		RevCommit second = add("file.txt", "b");
		CommitCountFilter count = new CommitCountFilter();

		CommitFinder finder = new CommitFinder(testRepo);
		finder.setFilter(new AndCommitFilter().add(new CommitterDateFilter(
				second.getCommitterIdent().getWhen()), count));
		finder.find();
		assertEquals(1, count.getCount());
	}

	/**
	 * Unit test of {@link CommitterDateFilter}
	 * 
	 * @throws Exception
	 */
	public void testCommitterNonMatch() throws Exception {
		RevCommit first = add("file.txt", "a");
		CommitCountFilter count = new CommitCountFilter();

		CommitFinder finder = new CommitFinder(testRepo);
		finder.setFilter(new AndCommitFilter().add(new CommitterDateFilter(
				first.getCommitterIdent().getWhen().getTime() + 1), count));
		finder.find();
		assertEquals(0, count.getCount());
	}

}
