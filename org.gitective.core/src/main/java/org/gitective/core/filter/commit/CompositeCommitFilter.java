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
package org.gitective.core.filter.commit;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.filter.RevFilter;

/**
 * Filter container other filters
 */
public abstract class CompositeCommitFilter extends CommitFilter {

	/**
	 * Child filters
	 */
	protected RevFilter[] filters;

	/**
	 * Create a composite filter with given child filters
	 *
	 * @param filters
	 */
	public CompositeCommitFilter(final RevFilter... filters) {
		if (filters != null && filters.length > 0) {
			this.filters = new RevFilter[filters.length];
			System.arraycopy(filters, 0, this.filters, 0, filters.length);
		} else
			this.filters = new RevFilter[0];
	}

	/**
	 * Add child filters to this filter.
	 * <p>
	 * This method resizes an internal array on each call so it should be called
	 * with as many child filters at once instead of once per child filter.
	 *
	 * @param addedFilters
	 * @return this filter
	 */
	public CompositeCommitFilter add(final RevFilter... addedFilters) {
		if (addedFilters == null)
			return this;
		final int added = addedFilters.length;
		if (added == 0)
			return this;
		final int current = filters.length;
		final RevFilter[] resized = new RevFilter[added + current];
		System.arraycopy(filters, 0, resized, 0, current);
		System.arraycopy(addedFilters, 0, resized, current, added);
		filters = resized;
		return this;
	}

	@Override
	public CommitFilter setRepository(final Repository repository) {
		for (RevFilter filter : filters)
			if (filter instanceof CommitFilter)
				((CommitFilter) filter).setRepository(repository);
		return super.setRepository(repository);
	}

	@Override
	public CommitFilter reset() {
		for (RevFilter filter : filters)
			if (filter instanceof CommitFilter)
				((CommitFilter) filter).reset();
		return super.reset();
	}

	/**
	 * Clone each filter into a new array.
	 *
	 * @return non-null but possibly empty array of child filters
	 */
	protected RevFilter[] cloneFilters() {
		final RevFilter[] copy = new RevFilter[filters.length];
		System.arraycopy(filters, 0, copy, 0, filters.length);
		return copy;
	}

	/**
	 * Get the number of filters that have been added as a child filter to this
	 * filter
	 *
	 * @return number of children filters
	 */
	public int getSize() {
		return filters.length;
	}
}
