package org.embulk.config;

public interface CommitReport
        extends DataSource
{
    @Override
    CommitReport getNested(String attrName);

    @Override
    CommitReport getNestedOrSetEmpty(String attrName);

    @Override
    CommitReport set(String attrName, Object v);

    @Override
    CommitReport setNested(String attrName, DataSource v);

    @Override
    CommitReport setAll(DataSource other);

    @Override
    CommitReport remove(String attrName);

    @Override
    CommitReport deepCopy();

    @Override
    CommitReport merge(DataSource other);
}
